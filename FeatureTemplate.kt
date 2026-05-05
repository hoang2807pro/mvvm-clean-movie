/**
 * ═══════════════════════════════════════════════════════════════
 *  TEMPLATE TẠO FEATURE MỚI
 *
 *  Cách dùng:
 *    1. Copy từng block code vào đúng file/package
 *    2. Tìm-replace toàn bộ:
 *         "Template"  →  tên feature viết hoa đầu  (VD: "Search")
 *         "template"  →  tên feature viết thường   (VD: "search")
 *    3. Điền TODO
 *    4. Đăng ký vào Core.kt + AndroidManifest.xml (xem cuối file)
 *
 *  Cấu trúc file cần tạo:
 *    features/template/
 *      Template.kt                          ← DI module
 *      failure/TemplateFailure.kt
 *      interactor/TemplateModel.kt
 *      interactor/GetTemplateList.kt
 *      data/TemplateApi.kt
 *      data/TemplateEntity.kt
 *      data/TemplateService.kt
 *      data/TemplateRepository.kt
 *      data/local/TemplateLocalEntity.kt
 *      data/local/TemplateDao.kt
 *      data/local/TemplateDatabase.kt
 *      ui/TemplateUiState.kt
 *      ui/TemplateViewModel.kt
 *      ui/TemplateFragment.kt
 *      ui/TemplateActivity.kt
 *    res/layout/fragment_template.xml
 * ═══════════════════════════════════════════════════════════════
 */

// ─────────────────────────────────────────────────────────────
// FILE: failure/TemplateFailure.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.failure

import com.fernandocejas.sample.core.failure.Failure

sealed class TemplateFailure : Failure.FeatureFailure() {
    object NotFound  : TemplateFailure()
    object ListEmpty : TemplateFailure()
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: interactor/TemplateModel.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.interactor

// Domain model — pure Kotlin, không import Android hay Retrofit
// TODO: thêm/đổi field cho phù hợp
data class TemplateModel(
    val id: Int,
    val name: String
)
*/


// ─────────────────────────────────────────────────────────────
// FILE: interactor/GetTemplateList.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.interactor

import com.fernandocejas.sample.core.interactor.UseCase
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.features.template.data.TemplateRepository

class GetTemplateList(
    private val repository: TemplateRepository
) : UseCase<List<TemplateModel>, None>() {

    override suspend fun run(params: None) = repository.getAll()
}

// Nếu UseCase cần tham số (VD: search theo query):
//
// class GetTemplateList(
//     private val repository: TemplateRepository
// ) : UseCase<List<TemplateModel>, GetTemplateList.Params>() {
//
//     override suspend fun run(params: Params) = repository.search(params.query)
//
//     data class Params(val query: String)
// }
*/


// ─────────────────────────────────────────────────────────────
// FILE: data/TemplateApi.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface TemplateApi {

    // TODO: thay endpoint thực tế
    @GET("templates.json")
    suspend fun getAll(): List<TemplateEntity>

    // Ví dụ path param:
    // @GET("templates/{id}.json")
    // suspend fun getById(@Path("id") id: Int): TemplateEntity

    // Ví dụ query param:
    // @GET("templates/search.json")
    // suspend fun search(@Query("q") query: String): List<TemplateEntity>
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: data/TemplateEntity.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.data

import com.fernandocejas.sample.features.template.interactor.TemplateModel

// Khớp với JSON trả về từ API
// TODO: thêm/đổi field
data class TemplateEntity(
    val id: Int,
    val name: String
) {
    fun toDomain() = TemplateModel(id, name)
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: data/TemplateService.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.data

import retrofit2.Retrofit

class TemplateService(retrofit: Retrofit) : TemplateApi {
    private val api by lazy { retrofit.create(TemplateApi::class.java) }

    override suspend fun getAll() = api.getAll()
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: data/local/TemplateLocalEntity.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fernandocejas.sample.features.template.interactor.TemplateModel

@Entity(tableName = "templates")
data class TemplateLocalEntity(
    @PrimaryKey val id: Int,
    val name: String
) {
    fun toDomain() = TemplateModel(id, name)
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: data/local/TemplateDao.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TemplateDao {

    @Query("SELECT * FROM templates")
    suspend fun getAll(): List<TemplateLocalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TemplateLocalEntity>)

    @Query("DELETE FROM templates")
    suspend fun deleteAll()
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: data/local/TemplateDatabase.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TemplateLocalEntity::class], version = 1, exportSchema = false)
abstract class TemplateDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: data/TemplateRepository.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.data

import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.failure.Failure.NetworkConnection
import com.fernandocejas.sample.core.failure.Failure.ServerError
import com.fernandocejas.sample.core.functional.Either
import com.fernandocejas.sample.core.functional.toLeft
import com.fernandocejas.sample.core.functional.toRight
import com.fernandocejas.sample.core.network.NetworkHandler
import com.fernandocejas.sample.features.template.data.local.TemplateDao
import com.fernandocejas.sample.features.template.data.local.TemplateLocalEntity
import com.fernandocejas.sample.features.template.interactor.TemplateModel

interface TemplateRepository {

    suspend fun getAll(): Either<Failure, List<TemplateModel>>

    class Network(
        private val networkHandler: NetworkHandler,
        private val service: TemplateService,
        private val dao: TemplateDao
    ) : TemplateRepository {

        // Online  → fetch API + lưu cache
        // Offline → đọc cache, báo lỗi nếu cache rỗng
        override suspend fun getAll(): Either<Failure, List<TemplateModel>> {
            return if (networkHandler.isNetworkAvailable()) {
                try {
                    val entities = service.getAll()
                    val models   = entities.map { it.toDomain() }
                    dao.insertAll(models.map { TemplateLocalEntity(it.id, it.name) })
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
*/


// ─────────────────────────────────────────────────────────────
// FILE: ui/TemplateUiState.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.ui

import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.features.template.interactor.TemplateModel

sealed class TemplateUiState {
    object Loading                                    : TemplateUiState()
    data class Success(val items: List<TemplateModel>) : TemplateUiState()
    data class Error(val failure: Failure)             : TemplateUiState()
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: ui/TemplateViewModel.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.ui

import androidx.lifecycle.viewModelScope
import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.core.platform.BaseViewModel
import com.fernandocejas.sample.features.template.interactor.GetTemplateList
import com.fernandocejas.sample.features.template.interactor.TemplateModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemplateViewModel(
    private val getTemplateList: GetTemplateList
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<TemplateUiState>(TemplateUiState.Loading)
    val uiState: StateFlow<TemplateUiState> = _uiState.asStateFlow()

    fun load() = getTemplateList(None(), viewModelScope) {
        it.fold(::onError, ::onSuccess)
    }

    private fun onSuccess(items: List<TemplateModel>) {
        _uiState.value = TemplateUiState.Success(items)
    }

    private fun onError(failure: Failure) {
        _uiState.value = TemplateUiState.Error(failure)
        handleFailure(failure)
    }
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: ui/TemplateFragment.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.fernandocejas.sample.R
import com.fernandocejas.sample.core.extension.invisible
import com.fernandocejas.sample.core.extension.visible
import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.failure.Failure.NetworkConnection
import com.fernandocejas.sample.core.failure.Failure.ServerError
import com.fernandocejas.sample.core.platform.BaseFragment
import com.fernandocejas.sample.databinding.FragmentTemplateBinding
import com.fernandocejas.sample.features.template.failure.TemplateFailure
import com.fernandocejas.sample.features.template.interactor.TemplateModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class TemplateFragment : BaseFragment() {

    private val viewModel: TemplateViewModel by inject()

    private var _binding: FragmentTemplateBinding? = null
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
        _binding = FragmentTemplateBinding.inflate(inflater, container, false)
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
                        is TemplateUiState.Loading -> showProgress()
                        is TemplateUiState.Success -> renderList(state.items)
                        is TemplateUiState.Error   -> handleFailure(state.failure)
                    }
                }
            }
        }
    }

    private fun renderList(items: List<TemplateModel>) {
        hideProgress()
        // TODO: bind items vào RecyclerView / views
    }

    private fun handleFailure(failure: Failure) {
        when (failure) {
            is NetworkConnection         -> renderFailure(R.string.failure_network_connection)
            is ServerError               -> renderFailure(R.string.failure_server_error)
            is TemplateFailure.ListEmpty -> renderFailure(R.string.failure_server_error)
            else                         -> renderFailure(R.string.failure_server_error)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        hideProgress()
        notifyWithAction(message, R.string.action_refresh) { viewModel.load() }
    }
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: ui/TemplateActivity.kt
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template.ui

import android.content.Context
import android.content.Intent
import com.fernandocejas.sample.core.platform.BaseActivity
import com.fernandocejas.sample.core.platform.BaseFragment

class TemplateActivity : BaseActivity() {

    companion object {
        fun callingIntent(context: Context) = Intent(context, TemplateActivity::class.java)
    }

    override fun fragment(): BaseFragment = TemplateFragment()
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: Template.kt  ← DI MODULE (đặt ở root package của feature)
// ─────────────────────────────────────────────────────────────
/*
package com.fernandocejas.sample.features.template

import androidx.room.Room
import com.fernandocejas.sample.core.Feature
import com.fernandocejas.sample.features.template.data.TemplateRepository
import com.fernandocejas.sample.features.template.data.TemplateService
import com.fernandocejas.sample.features.template.data.local.TemplateDatabase
import com.fernandocejas.sample.features.template.interactor.GetTemplateList
import com.fernandocejas.sample.features.template.ui.TemplateViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun templateFeature() = object : Feature {
    override fun name() = "template"
    override fun diModule() = module {

        // Room
        single {
            Room.databaseBuilder(androidContext(), TemplateDatabase::class.java, "template-db")
                .fallbackToDestructiveMigration()
                .build()
        }
        single { get<TemplateDatabase>().templateDao() }

        // Data
        factoryOf(::TemplateService)
        factory { TemplateRepository.Network(get(), get(), get()) } bind TemplateRepository::class

        // Domain
        factoryOf(::GetTemplateList)

        // UI
        viewModelOf(::TemplateViewModel)
    }
}
*/


// ─────────────────────────────────────────────────────────────
// FILE: res/layout/fragment_template.xml
// ─────────────────────────────────────────────────────────────
/*
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/templateList"
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
*/


// ═══════════════════════════════════════════════════════════════
//  SAU KHI TẠO XONG — 2 bước bắt buộc:
// ═══════════════════════════════════════════════════════════════
//
//  1. core/Core.kt — thêm feature vào danh sách:
//
//     fun allFeatures() = listOf(
//         coreFeature(),
//         authFeature(),
//         loginFeature(),
//         moviesFeature(),
//         templateFeature(),   // ← thêm dòng này
//     )
//
//  2. AndroidManifest.xml — đăng ký Activity:
//
//     <activity
//         android:name=".features.template.ui.TemplateActivity"
//         android:label="Template" />
//
//  (tuỳ chọn) Navigator.kt — thêm hàm navigate:
//
//     fun showTemplate(context: Context) =
//         context.startActivity(TemplateActivity.callingIntent(context))
//
// ═══════════════════════════════════════════════════════════════
