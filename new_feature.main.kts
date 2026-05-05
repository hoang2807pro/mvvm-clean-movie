#!/usr/bin/env kotlin
/**
 * Tạo boilerplate cho feature mới theo Clean Architecture.
 *
 * Cách chạy:
 *   kotlin new_feature.main.kts <TênFeature>
 *
 * Ví dụ:
 *   kotlin new_feature.main.kts Search
 *   kotlin new_feature.main.kts UserProfile
 */

import java.io.File

// ─── Validate input ───────────────────────────────────────────────────────────

if (args.isEmpty()) {
    println("Thiếu tên feature.")
    println("Dùng: kotlin new_feature.main.kts <TênFeature>")
    println("VD  : kotlin new_feature.main.kts Search")
    System.exit(1)
}

// ─── Tên biến ────────────────────────────────────────────────────────────────

val cap   = args[0].replaceFirstChar { it.uppercase() }   // Search
val low   = args[0].replaceFirstChar { it.lowercase() }   // search

val pkg      = "com.fernandocejas.sample"
val featPkg  = "$pkg.features.$low"
val baseDir  = "app/src/main/kotlin/com/fernandocejas/sample/features/$low"
val resDir   = "app/src/main/res"

// ─── Helpers ─────────────────────────────────────────────────────────────────

fun write(path: String, content: String) {
    val file = File(path)
    file.parentFile.mkdirs()
    file.writeText(content.trimIndent() + "\n")
    println("  created  $path")
}

println("\n▶ Tạo feature: $cap  ($featPkg)\n")

// ══════════════════════════════════════════════════════════════════════════════
// 1. FAILURE
// ══════════════════════════════════════════════════════════════════════════════

write("$baseDir/failure/${cap}Failure.kt", """
    package $featPkg.failure

    import $pkg.core.failure.Failure

    sealed class ${cap}Failure : Failure.FeatureFailure() {
        object NotFound  : ${cap}Failure()
        object ListEmpty : ${cap}Failure()
    }
""")

// ══════════════════════════════════════════════════════════════════════════════
// 2. DOMAIN LAYER
// ══════════════════════════════════════════════════════════════════════════════

write("$baseDir/interactor/${cap}Model.kt", """
    package $featPkg.interactor

    // TODO: thêm/đổi field cho phù hợp với domain
    data class ${cap}Model(
        val id: Int,
        val name: String
    )
""")

write("$baseDir/interactor/Get${cap}List.kt", """
    package $featPkg.interactor

    import $pkg.core.interactor.UseCase
    import $pkg.core.interactor.UseCase.None
    import $featPkg.data.${cap}Repository

    class Get${cap}List(
        private val repository: ${cap}Repository
    ) : UseCase<List<${cap}Model>, None>() {

        override suspend fun run(params: None) = repository.getAll()
    }
""")

// ══════════════════════════════════════════════════════════════════════════════
// 3. DATA LAYER
// ══════════════════════════════════════════════════════════════════════════════

write("$baseDir/data/${cap}Api.kt", """
    package $featPkg.data

    import retrofit2.http.GET

    internal interface ${cap}Api {

        // TODO: thay bằng endpoint thực tế
        @GET("${low}s.json")
        suspend fun getAll(): List<${cap}Entity>
    }
""")

write("$baseDir/data/${cap}Entity.kt", """
    package $featPkg.data

    import $featPkg.interactor.${cap}Model

    // TODO: thêm/đổi field khớp với JSON response
    data class ${cap}Entity(
        val id: Int,
        val name: String
    ) {
        fun toDomain() = ${cap}Model(id, name)
    }
""")

write("$baseDir/data/${cap}Service.kt", """
    package $featPkg.data

    import retrofit2.Retrofit

    class ${cap}Service(retrofit: Retrofit) : ${cap}Api {
        private val api by lazy { retrofit.create(${cap}Api::class.java) }

        override suspend fun getAll() = api.getAll()
    }
""")

write("$baseDir/data/local/${cap}LocalEntity.kt", """
    package $featPkg.data.local

    import androidx.room.Entity
    import androidx.room.PrimaryKey
    import $featPkg.interactor.${cap}Model

    @Entity(tableName = "${low}s")
    data class ${cap}LocalEntity(
        @PrimaryKey val id: Int,
        val name: String
    ) {
        fun toDomain() = ${cap}Model(id, name)
    }
""")

write("$baseDir/data/local/${cap}Dao.kt", """
    package $featPkg.data.local

    import androidx.room.Dao
    import androidx.room.Insert
    import androidx.room.OnConflictStrategy
    import androidx.room.Query

    @Dao
    interface ${cap}Dao {

        @Query("SELECT * FROM ${low}s")
        suspend fun getAll(): List<${cap}LocalEntity>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(items: List<${cap}LocalEntity>)

        @Query("DELETE FROM ${low}s")
        suspend fun deleteAll()
    }
""")

write("$baseDir/data/local/${cap}Database.kt", """
    package $featPkg.data.local

    import androidx.room.Database
    import androidx.room.RoomDatabase

    @Database(entities = [${cap}LocalEntity::class], version = 1, exportSchema = false)
    abstract class ${cap}Database : RoomDatabase() {
        abstract fun ${low}Dao(): ${cap}Dao
    }
""")

write("$baseDir/data/${cap}Repository.kt", """
    package $featPkg.data

    import $pkg.core.failure.Failure
    import $pkg.core.failure.Failure.NetworkConnection
    import $pkg.core.failure.Failure.ServerError
    import $pkg.core.functional.Either
    import $pkg.core.functional.toLeft
    import $pkg.core.functional.toRight
    import $pkg.core.network.NetworkHandler
    import $featPkg.data.local.${cap}Dao
    import $featPkg.data.local.${cap}LocalEntity
    import $featPkg.interactor.${cap}Model

    interface ${cap}Repository {

        suspend fun getAll(): Either<Failure, List<${cap}Model>>

        class Network(
            private val networkHandler: NetworkHandler,
            private val service: ${cap}Service,
            private val dao: ${cap}Dao
        ) : ${cap}Repository {

            // Online  → fetch API + lưu cache
            // Offline → đọc cache, báo lỗi nếu cache rỗng
            override suspend fun getAll(): Either<Failure, List<${cap}Model>> {
                return if (networkHandler.isNetworkAvailable()) {
                    try {
                        val entities = service.getAll()
                        val models   = entities.map { it.toDomain() }
                        dao.insertAll(models.map { ${cap}LocalEntity(it.id, it.name) })
                        models.toRight()
                    } catch (e: Exception) {
                        ServerError.toLeft()
                    }
                } else {
                    val cached = dao.getAll()
                    if (cached.isNotEmpty()) cached.map { it.toDomain() }.toRight()
                    else NetworkConnection.toLeft()
                }
            }
        }
    }
""")

// ══════════════════════════════════════════════════════════════════════════════
// 4. UI LAYER
// ══════════════════════════════════════════════════════════════════════════════

write("$baseDir/ui/${cap}UiState.kt", """
    package $featPkg.ui

    import $pkg.core.failure.Failure
    import $featPkg.interactor.${cap}Model

    sealed class ${cap}UiState {
        object Loading                                     : ${cap}UiState()
        data class Success(val items: List<${cap}Model>)   : ${cap}UiState()
        data class Error(val failure: Failure)              : ${cap}UiState()
    }
""")

write("$baseDir/ui/${cap}ViewModel.kt", """
    package $featPkg.ui

    import androidx.lifecycle.viewModelScope
    import $pkg.core.failure.Failure
    import $pkg.core.interactor.UseCase.None
    import $pkg.core.platform.BaseViewModel
    import $featPkg.interactor.Get${cap}List
    import $featPkg.interactor.${cap}Model
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow

    class ${cap}ViewModel(
        private val get${cap}List: Get${cap}List
    ) : BaseViewModel() {

        private val _uiState = MutableStateFlow<${cap}UiState>(${cap}UiState.Loading)
        val uiState: StateFlow<${cap}UiState> = _uiState.asStateFlow()

        fun load() = get${cap}List(None(), viewModelScope) {
            it.fold(::onError, ::onSuccess)
        }

        private fun onSuccess(items: List<${cap}Model>) {
            _uiState.value = ${cap}UiState.Success(items)
        }

        private fun onError(failure: Failure) {
            _uiState.value = ${cap}UiState.Error(failure)
            handleFailure(failure)
        }
    }
""")

write("$baseDir/ui/${cap}Fragment.kt", """
    package $featPkg.ui

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.annotation.StringRes
    import androidx.lifecycle.Lifecycle
    import androidx.lifecycle.lifecycleScope
    import androidx.lifecycle.repeatOnLifecycle
    import $pkg.R
    import $pkg.core.extension.invisible
    import $pkg.core.extension.visible
    import $pkg.core.failure.Failure
    import $pkg.core.failure.Failure.NetworkConnection
    import $pkg.core.failure.Failure.ServerError
    import $pkg.core.platform.BaseFragment
    import $pkg.databinding.Fragment${cap}Binding
    import $featPkg.failure.${cap}Failure
    import $featPkg.interactor.${cap}Model
    import kotlinx.coroutines.launch
    import org.koin.android.ext.android.inject

    class ${cap}Fragment : BaseFragment() {

        private val viewModel: ${cap}ViewModel by inject()

        private var _binding: Fragment${cap}Binding? = null
        private val binding get() = _binding!!

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            observeViewModel()
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = Fragment${cap}Binding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel.load()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        private fun observeViewModel() {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            is ${cap}UiState.Loading -> showProgress()
                            is ${cap}UiState.Success -> renderList(state.items)
                            is ${cap}UiState.Error   -> handleFailure(state.failure)
                        }
                    }
                }
            }
        }

        private fun renderList(items: List<${cap}Model>) {
            hideProgress()
            // TODO: bind items vào RecyclerView / views
        }

        private fun handleFailure(failure: Failure) {
            when (failure) {
                is NetworkConnection          -> renderFailure(R.string.failure_network_connection)
                is ServerError                -> renderFailure(R.string.failure_server_error)
                is ${cap}Failure.ListEmpty    -> renderFailure(R.string.failure_server_error)
                else                          -> renderFailure(R.string.failure_server_error)
            }
        }

        private fun renderFailure(@StringRes message: Int) {
            hideProgress()
            notifyWithAction(message, R.string.action_refresh) { viewModel.load() }
        }
    }
""")

write("$baseDir/ui/${cap}Activity.kt", """
    package $featPkg.ui

    import android.content.Context
    import android.content.Intent
    import $pkg.core.platform.BaseActivity
    import $pkg.core.platform.BaseFragment

    class ${cap}Activity : BaseActivity() {

        companion object {
            fun callingIntent(context: Context) = Intent(context, ${cap}Activity::class.java)
        }

        override fun fragment(): BaseFragment = ${cap}Fragment()
    }
""")

// ══════════════════════════════════════════════════════════════════════════════
// 5. DI MODULE
// ══════════════════════════════════════════════════════════════════════════════

write("$baseDir/${cap}.kt", """
    package $featPkg

    import androidx.room.Room
    import $pkg.core.Feature
    import $featPkg.data.${cap}Repository
    import $featPkg.data.${cap}Service
    import $featPkg.data.local.${cap}Database
    import $featPkg.interactor.Get${cap}List
    import $featPkg.ui.${cap}ViewModel
    import org.koin.android.ext.koin.androidContext
    import org.koin.androidx.viewmodel.dsl.viewModelOf
    import org.koin.core.module.dsl.factoryOf
    import org.koin.dsl.bind
    import org.koin.dsl.module

    fun ${low}Feature() = object : Feature {
        override fun name() = "$low"
        override fun diModule() = module {

            // Room
            single {
                Room.databaseBuilder(androidContext(), ${cap}Database::class.java, "$low-db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            single { get<${cap}Database>().${low}Dao() }

            // Data
            factoryOf(::${cap}Service)
            factory { ${cap}Repository.Network(get(), get(), get()) } bind ${cap}Repository::class

            // Domain
            factoryOf(::Get${cap}List)

            // UI
            viewModelOf(::${cap}ViewModel)
        }
    }
""")

// ══════════════════════════════════════════════════════════════════════════════
// 6. XML LAYOUT
// ══════════════════════════════════════════════════════════════════════════════

write("$resDir/layout/fragment_${low}.xml", """
    <?xml version="1.0" encoding="utf-8"?>
    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/${low}List"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

        <TextView
            android:id="@+id/emptyView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:visibility="gone"
            android:text="Không có dữ liệu" />

    </FrameLayout>
""")

// ══════════════════════════════════════════════════════════════════════════════
// DONE
// ══════════════════════════════════════════════════════════════════════════════

println("""

✅ Tạo xong feature "$cap"!

Còn 2 bước làm thủ công:

  1. core/Core.kt — thêm vào allFeatures():
       ${low}Feature(),

  2. AndroidManifest.xml — đăng ký Activity:
       <activity
           android:name=".features.$low.ui.${cap}Activity"
           android:label="$cap" />

  (tuỳ chọn) Navigator.kt — thêm hàm navigate:
       fun show${cap}(context: Context) =
           context.startActivity(${cap}Activity.callingIntent(context))
""")
