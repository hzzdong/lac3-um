<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="应用基本信息" name="tabApp">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" circle plain @click="onRefresh()" />
          <el-button v-permission="['yw_app_edit']" type="success" icon="el-icon-edit" circle @click="toAppUpdate()" />
          <el-button v-if="app.status === 0 && checkPermission(['yw_app_edit'])" type="warning" icon="el-icon-lock" title="禁用" circle @click="toChangeAppStatus(1)" />
          <el-button v-if="app.status === 1 && checkPermission(['yw_app_edit'])" type="warning" icon="el-icon-unlock" title="启用" circle @click="toChangeAppStatus(0)" />
          <el-button v-permission="['yw_app_edit']" type="danger" icon="el-icon-delete" circle @click="onAppDelete()" />
        </div>
        <aside style="margin-top:15px;">
          <i class="el-icon-info" /> 应用视图。您可以通过选择本视图右侧的页签查看和编辑相关功能信息。
        </aside>
        <el-form
          ref="appForm"
          :model="app"
          size="small"
          status-icon
          label-position="right"
          label-width="120px"
          style="width: 98%;"
        >
          <el-row>
            <el-col :span="12">
              <el-form-item label="应用名称:" prop="name">
                <span class="el-span_view">{{ app.name }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="编号:" prop="code">
                <span class="el-span_view">{{ app.code }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="类别:" prop="clazz">
                <span class="el-span_view">{{ app.clazz | appClazzFilter }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="内外类型:" prop="type">
                <span class="el-span_view">{{ app.type | appTypeFilter }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="账号映射类型:" prop="mappingType">
                <span class="el-span_view">{{ app.mappingType | accountMappringTypeFilter }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="适合屏幕类型:" prop="screenType">
                <span class="el-span_view">{{ app.screenType | screenTypeFilter }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="状态:" prop="status">
                <span class="el-span_view">{{ app.status | statusFilter }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="排序号:" prop="sort">
                <span class="el-span_view">{{ app.sort }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="首页地址:" prop="url">
            <span class="el-span_view">{{ app.url }}</span>
          </el-form-item>
          <el-form-item label="登出地址:" prop="logout">
            <span class="el-span_view">{{ app.logout }}</span>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="创建时间:" prop="createTime">
                <span class="el-span_view">{{ app.createTime }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后更新:" prop="updateTime">
                <span class="el-span_view">{{ app.updateTime }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="备注:">
            <el-input v-model="app.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" class="el-textarea_view" />
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="接口安全设置" name="tabAppInterface">
        <aside>
          <i class="el-icon-info" /> 应用[ <span>{{ app.name }}</span> ]安全信息设置。
        </aside>
        <el-form
          ref="appInterfaceForm"
          :model="app"
          :rules="rules"
          size="small"
          status-icon
          label-position="right"
          label-width="120px"
          style="width: 98%;"
        >
          <el-row>
            <el-col :span="12">
              <el-form-item label="应用名称:" prop="name">
                <span class="el-span_view">{{ app.name }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="超时时间" prop="timeout">
                <el-input v-model.number="app.timeout" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="签名算法" prop="signatureAlg">
                <el-radio-group v-model="app.signatureAlg" size="small">
                  <el-radio-button v-for="item in signAlgs" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="签名秘钥" prop="signatureKey">
                <el-input v-model="app.signatureKey" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="加密算法" prop="messageEncAlg">
                <el-radio-group v-model="app.messageEncAlg" size="small">
                  <el-radio-button label="">明文(不加密)</el-radio-button>
                  <el-radio-button label="AES">AES</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="加密秘钥" prop="messageEncKey">
                <el-input v-model="app.messageEncKey" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="白名单" prop="host">
            <el-input v-model="app.host" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入应用白名单" />
          </el-form-item>
          <el-form-item>
            <el-button v-permission="['yw_app_edit']" type="primary" style="margin-left: 10px; float: right;" @click="onSaveAppInterface">保存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane v-if="app.mappingType === 2" label="映射接口设置" name="tabAppMapping">
        <aside>
          <i class="el-icon-info" /> 应用[ <span>{{ app.name }}</span> ]账号映射模式，接口认证安全信息设置。
        </aside>
        <el-form
          ref="appMappingInterfaceForm"
          :model="app"
          :rules="rules"
          size="small"
          status-icon
          label-position="right"
          label-width="120px"
          style="width: 98%;"
        >
          <el-row>
            <el-col :span="24">
              <el-form-item label="应用名称:" prop="name">
                <span class="el-span_view">{{ app.name }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="接口地址" prop="authAddr">
                <el-input v-model.number="app.authAddr" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="密码模式" prop="authPassMode">
                <el-radio-group v-model="app.authPassMode" size="small">
                  <el-radio-button label="MD5">MD5</el-radio-button>
                  <el-radio-button label="">明文(不加密)</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="签名算法" prop="authSignAlg">
                <el-radio-group v-model="app.authSignAlg" size="small">
                  <el-radio-button v-for="item in signAlgs" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="签名秘钥" prop="authSignKey">
                <el-input v-model="app.authSignKey" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="说明">
            <aside>
              <dl>
                <dt>1. 调用时机</dt>
                <dd>根据以上设置，当用户首次访问此应用时，SSO会发起账户绑定操作。用户在绑定界面上输入账号信息后，SSO会以rest方式调用应用的账号认证接口</dd>
                <dt>2. 接口调用方式</dt>
                <dd>POST调用，账号认证接口地址?loginName=XXX&password=XXX&time=XXX&sign=XXX</dd>
                <dt>3. 参数说明</dt>
                <dd>（1）password=原文或者MD5(原文)</dd>
                <dd>（2）time=当前时间new Date().getTime()</dd>
                <dd>（3）sign=MD5/SHA1(loginName+password+time+签名密钥)</dd>
                <dt>4. 接口返回</dt>
                <dd>接口返回JSON结果如下：</dd>
                <dd>（1）成功：{code:"0",message:"成功"}</dd>
                <dd>（2）失败：{code:"XXX",message:"失败原因"}</dd>
              </dl>
            </aside>
          </el-form-item>
          <el-form-item>
            <el-button v-permission="['yw_app_edit']" type="primary" style="margin-left: 10px; float: right;" @click="onSaveAppMappingInterface">保存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane v-if="checkPermission(['yw_app_menu'])" label="应用菜单管理" name="tabAppMenu">
        <aside>
          <i class="el-icon-info" /> 应用[ <span>{{ app.name }}</span> ]菜单管理。
        </aside>
        <el-table
          ref="menuTable"
          :key="menu.tableKey"
          v-loading="menu.listLoading"
          :data="menu.list"
          style="width: 100%;margin-bottom: 20px;"
          row-key="id"
          border
          fit
          highlight-current-row
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column label="菜单名称" min-width="200" prop="name">
            <template slot-scope="{row}">
              <span v-if="row.id === '0'">{{ row.name }}</span>
              <router-link v-if="row.id !== '0'" :to="'/application/menu-view/'+row.id+'/'+row.uuid" class="link-type">
                <span>{{ row.name }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="govCode" label="编号" width="300">
            <template slot-scope="{row}">
              <span v-if="row.id !== '0'">{{ row.govCode }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="类型" class-name="status-col" width="90">
            <template slot-scope="{row}">
              <el-tag v-if="row.id !== '0'" size="small" :type="row.type | statusTypeFilter">{{ row.type | menuTypeFilter }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" class-name="status-col" width="70">
            <template slot-scope="{row}">
              <el-tag v-if="row.id !== '0'" effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | userStatusFilter" />
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序号" width="90">
            <template slot-scope="{row}">
              <span v-if="row.id !== '0'">{{ row.sort }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="60">
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" icon="el-icon-plus" title="新增子菜单" @click="handleAddMenu(row)" />
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="菜单操作清单" name="tabAppMenuOperations">
        <aside>
          <i class="el-icon-info" /> 应用[ <span>{{ app.name }}</span> ] 所有菜单项关联操作清单。
        </aside>
        <div class="filter-container">
          <el-input v-model="operations.listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="handleAppMenuOperationsFilter" />
          <el-input v-model="operations.listQuery.rules.uri.fv" placeholder="uri 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-select v-model="operations.listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleAppMenuOperationsFilter" />
          <el-button
            v-permission="['yw_app_menu']"
            class="filter-item"
            style="float: right;"
            type="primary"
            icon="el-icon-plus"
            @click="toCreateOperation"
          >
            添加操作
          </el-button>
        </div>
        <el-table
          ref="operationTable"
          :key="operations.tableKey"
          v-loading="operations.listLoading"
          :data="operations.list"
          style="width: 100%;margin-bottom: 20px;"
          row-key="id"
          border
          fit
          highlight-current-row
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="status" label="" class-name="status-col" width="40">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | userStatusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="归属菜单" width="200">
            <template slot-scope="{row}">
              <span>{{ row.menuName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作名称" width="300" prop="name">
            <template slot-scope="{row}">
              <span>{{ row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="uri" label="URI" min-width="200">
            <template slot-scope="{row}">
              <span>{{ row.uri }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="100" class-name="small-padding">
            <template slot-scope="{row}">
              <el-button type="primary" size="mini" icon="el-icon-edit" title="编辑操作" @click="toEditOperaton(row)" />
              <el-button type="danger" size="mini" icon="el-icon-close" title="移除操作" @click="onOperatonDelete(row)" />
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="operations.total>0" :total="operations.total" :page.sync="operations.listQuery.page" :limit.sync="operations.listQuery.limit" @pagination="findAppMenuOperations" />
      </el-tab-pane>
      <el-tab-pane label="应用图标上传" name="tabAppIco">
        <aside>
          <i class="el-icon-info" /> 应用[ <span>{{ app.name }}</span> ] 图标上传。
        </aside>
        <div class="filter-container">
          <span>
            <my-upload
              v-model="show"
              field="file"
              :width="300"
              :height="300"
              url="/fss/pub/Archive/upload"
              :params="params"
              :headers="headers"
              img-format="png"
              @crop-success="cropSuccess"
              @crop-upload-success="cropUploadSuccess"
              @crop-upload-fail="cropUploadFail"
            />
            <el-avatar :size="100" :src="app.ico" />
            <el-button type="primary" icon="el-icon-upload" circle title="应用图标上传" @click="toggleShow()" />
          </span>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="应用编辑" :visible.sync="dialogVisible">
      <el-form ref="appForm" :rules="rules" :inline="false" :model="entity" size="small" status-icon label-position="right" label-width="120px" style="width: 90%; margin-left:30px;">
        <el-form-item label="应用名称" prop="name">
          <el-input v-model="entity.name" />
        </el-form-item>
        <el-form-item label="应用编号" prop="code">
          <el-input v-model="entity.code" :disabled="true" />
        </el-form-item>
        <el-form-item label="应用类别" prop="clazz">
          <el-radio-group v-model="entity.clazz" disabled size="small">
            <el-radio-button v-for="item in appClazz" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内外类型" prop="type">
          <el-radio-group v-model="entity.type" disabled size="small">
            <el-radio-button v-for="item in appTypeOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号映射类型" prop="mappingType">
          <el-radio-group v-model="entity.mappingType" disabled size="small">
            <el-radio-button v-for="item in accountMappringTypes" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="适用屏幕" prop="screenType">
          <el-radio-group v-model="entity.screenType" size="small">
            <el-radio-button v-for="item in screenTypes" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="entity.status" size="small">
            <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="首页地址" prop="url">
          <el-input v-model="entity.url" />
        </el-form-item>
        <el-form-item label="登出地址" prop="logout">
          <el-input v-model="entity.logout" />
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model.number="entity.sort" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
        </el-form-item>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="onAppUpdate()">保存</el-button>
        </div>
      </el-form></el-dialog>

    <el-dialog title="新增子菜单" :visible.sync="menu.dialogVisible" width="50%">
      <el-form ref="menuForm" :rules="menu.rules" :model="menu.entity" size="small" status-icon label-position="right" label-width="80px" style="width: 90%; margin-left:30px;">
        <el-form-item label="父菜单">
          <el-input v-model="menu.entity.parentName" disabled="true" />
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menu.entity.name" />
        </el-form-item>
        <el-form-item label="编号" prop="govCode">
          <el-input v-model="menu.entity.govCode" />
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <el-input v-model="menu.entity.url" />
        </el-form-item>
        <el-form-item label="字体图标" prop="ico">
          <el-input v-model="menu.entity.ico" />
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input v-model.number="menu.entity.sort" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="menu.entity.type" size="small">
            <el-radio-button v-for="item in menu.menuTypes" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="menu.entity.status" size="small">
            <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="menu.entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menu.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createMenu()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="operations.dialogHeaderMap[operations.dialogStatus]" :visible.sync="operations.dialogVisible" width="50%">
      <el-form ref="operationForm" :rules="operations.rules" :inline="false" :model="operations.entity" size="small" status-icon label-post="right" label-width="80px" style="width: 98%; margin-left:10px;">
        <el-form-item label="归属菜单" prop="menuName">
          <el-input v-model="operations.entity.menuName" :disabled="true">
            <el-button slot="append" icon="el-icon-search" @click="toSelectMenu()" />
          </el-input>
        </el-form-item>
        <el-form-item label="操作名称" prop="name">
          <el-input v-model="operations.entity.name" placeholder="请输入操作名称" />
        </el-form-item>
        <el-form-item label="URI" prop="uri">
          <el-input v-model="operations.entity.uri" placeholder="请输入操作URI" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="operations.entity.status" size="small">
            <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="operations.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="operations.dialogStatus==='create'?onAddOperation():onUpdateOperation()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog id="dlg-menu-select4app-view" title="菜单选择" :visible.sync="ms.dialogVisible" width="50%">
      <lac-menu-single-select
        :app-id="ms.appId"
        :app-uuid="ms.appUuid"
        :with-buttons="ms.withButtons"
        @onMenuSingleSelected="onMenuSelected"
      />
    </el-dialog>

  </div>
</template>

<script>
import { fetchById, updateApp, deleteApp, changeAppStatus, updateAppInterface, updateAppMappingInterface, saveAppIco } from '@/api/application'
import { loadAppMenuTree, saveMenu, getMenuOperationPage, saveOperation, deleteOperation } from '@/api/menu'
import { sheetClose, sheetRefresh, autoChildren } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { validURL } from '@/utils/validate'
import waves from '@/directive/waves' // waves directive
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数
import LacMenuSingleSelect from '@/views/menu/components/menu-single-select'
import { statusOptions, appTypeOptions, appClazz, accountMappringTypes, screenTypes, signAlgs, encAlgs, menuTypes } from '@/filters'
import myUpload from 'vue-image-crop-upload'

export default {
  name: 'AppDetail',
  components: { Pagination, LacMenuSingleSelect, 'my-upload': myUpload },
  directives: { waves, permission },
  data() {
    return {
      tabPosition: 'right',
      activeTab: 'tabApp',
      tempRoute: {},
      app: {},
      entity: {}, // for edit
      statusOptions: statusOptions(),
      appTypeOptions: appTypeOptions(),
      appClazz: appClazz(),
      accountMappringTypes: accountMappringTypes(),
      screenTypes: screenTypes(),
      signAlgs: signAlgs(),
      encAlgs: encAlgs(),
      rules: {
        name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
        code: [{ required: true, message: '编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到32 个字符', trigger: 'blur' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }],
        timeout: [{ required: true, message: '超时时间不能为空' }, { type: 'number', message: '超时时间必须为数字值' }],
        signatureKey: [{ required: true, message: '签名秘钥不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '签名秘钥长度在 2 到 64 个字符', trigger: 'blur' }],
        host: [{ required: true, message: '白名单不能为空', trigger: 'blur' }, { min: 2, max: 512, message: '白名单长度在 2 到512 个字符', trigger: 'blur' }],
        authAddr: [{ required: true, message: '接口地址不能为空', trigger: 'blur' }, { min: 10, max: 512, message: '接口地址长度在 10 到512 个字符', trigger: 'blur' }],
        authSignKey: [{ required: true, message: '签名秘钥不能为空', trigger: 'blur' }, { min: 10, max: 64, message: '签名秘钥长度在 10 到64 个字符', trigger: 'blur' }]
      },
      dialogVisible: false,
      loading: false,
      /** menu */
      menu: {
        currentNode: undefined,
        dialogVisible: false,
        loaded: false,
        tableKey: 0,
        list: [],
        listLoading: true,
        entity: {},
        rules: {
          name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
          govCode: [{ required: true, message: '编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到32 个字符', trigger: 'blur' }],
          url: [
            { validator: (rule, value, callback) => {
              if (value && value !== '') {
                if (!validURL(value)) {
                  callback(new Error('请输入有效的URL地址'))
                  return
                }
              }
              callback()
            }, trigger: 'blur' }
          ],
          sort: [{ required: true, message: '排序号不能为空' }, { type: 'number', message: '排序号必须为数字值' }],
          remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
        },
        menuTypes: menuTypes()
      },
      /** operations */
      operations: {
        dialogVisible: false,
        dialogStatus: '',
        dialogHeaderMap: {
          update: '编辑操作',
          create: '新增操作'
        },
        loaded: false,
        tableKey: 1,
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            appId: { fv: undefined, oper: 'eq', stype: 'L' },
            menuId: { fv: undefined, oper: 'eq', stype: 'L' },
            menuUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            uri: { fv: undefined, oper: 'cn', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        entity: {},
        rules: {
          menuName: [{ required: true, message: '归属菜单不能为空，请选择', trigger: 'blur' }],
          name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
          uri: [{ required: true, message: 'uri不能为空', trigger: 'blur' }, { max: 255, message: 'uri长度不能大于 255 个字符', trigger: 'blur' }],
          remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
        }
      },
      ms: {
        dialogVisible: false,
        appId: 0,
        appUuid: '',
        withButtons: true
      },
      /* ICO */
      show: false,
      params: {
        appId: '1',
        appName: '用户中心',
        objectId: '',
        objectType: '10003'
      },
      headers: {
        smail: '*_~'
      }

    }
  },
  computed: {
    // TODO
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    const uuid = this.$route.params && this.$route.params.uuid
    this.fetchData(id, uuid)
    this.tempRoute = Object.assign({}, this.$route)
  },
  methods: {
    checkPermission,
    fetchData(id, uuid) {
      const that = this
      fetchById({ id, uuid }).then(response => {
        that.app = response.data
        that.entity = Object.assign({ dataType: 'Object' }, that.app)
        that.ms.appId = that.app.id
        that.ms.appUuid = that.app.uuid
        that.params.objectId = that.app.uuid
        if (that.app.ico) {
          that.app.ico = that.$store.state.settings.fssBaseUrl + that.app.ico
        }
        that.setTagsViewTitle()
        that.setPageTitle()
      }).catch(err => {
        console.log(err)
      })
    },
    setTagsViewTitle() {
      const title = '应用视图'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.app.name}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = '应用视图'
      document.title = `${title} - ${this.app.name}`
    },
    handleTabClick(tab, event) {
      if (tab.name === 'tabAppMenu') {
        if (this.menu.loaded === false) {
          this.findAppMenuTree()
          this.menu.loaded = true
        }
      } else if (tab.name === 'tabAppMenuOperations') {
        if (this.operations.loaded === false) {
          this.findAppMenuOperations()
          this.operations.loaded = true
        }
      }
    },
    toAppUpdate() {
      const that = this
      that.entity = Object.assign({ dataType: 'Object' }, that.app)
      that.dialogVisible = true
      that.$nextTick(() => {
        that.$refs['appForm'].clearValidate()
      })
    },
    onAppUpdate() {
      const that = this
      that.$refs['appForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({ dataType: 'Object' }, that.entity)
          updateApp(tempData).then(() => {
            that.app = Object.assign({}, that.entity)
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    toChangeAppStatus(status) {
      this.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { id: this.app.id, uuid: this.app.uuid, status: status }
        changeAppStatus(req).then(() => {
          this.app.status = status
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch((e) => console.log(e))
      }).catch((e) => console.log(e))
    },
    onAppDelete() {
      const that = this
      that.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const { id, uuid } = that.app
        deleteApp({ id, uuid }).then(() => {
          that.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
          that.onClose()
        }).catch((err) => console.log(err))
      }).catch((err) => console.log(err))
    },
    onClose() {
      sheetClose(this)
    },
    onRefresh() {
      sheetRefresh(this)
    },
    /* 接口安全信息保存 */
    onSaveAppInterface() {
      const that = this
      that.$refs['appInterfaceForm'].validate((valid) => {
        if (valid) {
          const { dataType = 'Object', id, uuid, timeout, signatureAlg, signatureKey, messageEncAlg, messageEncKey, host } = that.app
          updateAppInterface({ dataType, id, uuid, timeout, signatureAlg, signatureKey, messageEncAlg, messageEncKey, host }).then(() => {
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    /* 账号映射模式信息保存 */
    onSaveAppMappingInterface() {
      const that = this
      that.$refs['appMappingInterfaceForm'].validate((valid) => {
        if (valid) {
          const { dataType = 'Object', id, uuid, authAddr, authPassMode, authSignAlg, authSignKey } = that.app
          updateAppMappingInterface({ dataType, id, uuid, authAddr, authPassMode, authSignAlg, authSignKey }).then(() => {
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    /** menu */
    findAppMenuTree() {
      const that = this
      const { id, uuid } = that.app
      that.menu.listLoading = true
      loadAppMenuTree({ id, uuid }).then(response => {
        const items = response.data
        if (items && items.length > 0) {
          autoChildren(items)
          that.menu.list = items
        }
        that.menu.listLoading = false
      }).catch(err => console.log(err))
    },
    resetMenuEntity(parent) {
      this.menu.entity = {
        dataType: 'Object',
        appId: undefined,
        parentId: parent.id,
        parentName: parent.name,
        id: undefined,
        uuid: '',
        name: '',
        govCode: '',
        url: '',
        ico: '',
        type: 0,
        status: 0,
        sort: 1,
        remark: ''
      }
    },
    handleAddMenu(row) {
      const that = this
      that.menu.currentNode = row
      that.resetMenuEntity(row)
      this.menu.entity.appId = that.app.id
      that.menu.dialogVisible = true
      that.$nextTick(() => {
        that.$refs['menuForm'].clearValidate()
      })
    },
    createMenu() {
      const that = this
      that.$refs['menuForm'].validate((valid) => {
        if (valid) {
          saveMenu(that.menu.entity).then((response) => {
            const tempData = Object.assign({ dataType: 'Object', children: [] }, response.data)
            if (!that.menu.currentNode.children) {
              that.$set(that.menu.currentNode, 'children', [])
              that.$set(that.menu.currentNode.children, 0, tempData)
            } else {
              that.menu.currentNode.children.push(tempData)
            }
            that.menu.dialogVisible = false
            that.$notify({ title: '提示', message: '菜单创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    /** Operations */
    findAppMenuOperations() {
      const that = this
      that.operations.listQuery.rules.appId.fv = that.app.id
      that.operations.listLoading = true
      getMenuOperationPage(that.operations.listQuery).then(response => {
        that.operations.list = response.data.data
        that.operations.total = response.data.recordsTotal
        that.operations.listLoading = false
      })
    },
    handleAppMenuOperationsFilter() {
      this.operations.listQuery.page = 1
      this.findAppMenuOperations()
    },
    resetOperationEntity(me) {
      const that = this
      if (me) {
        that.operations.entity = Object.assign({ dataType: 'Object' }, me)
      } else {
        const old = that.operations.entity
        that.operations.entity = {
          dataType: 'Object',
          appId: that.app.id,
          menuId: old.menuId,
          menuName: old.menuName,
          menuGovCode: old.menuGovCode,
          name: '',
          uri: '',
          status: 0,
          remark: ''
        }
      }
    },
    toCreateOperation() {
      this.operations.dialogVisible = true
      this.operations.dialogStatus = 'create'
      this.resetOperationEntity()
      this.$nextTick(() => {
        this.$refs['operationForm'].clearValidate()
      })
    },
    toSelectMenu() {
      this.ms.dialogVisible = true
    },
    onMenuSelected(menu) {
      const that = this
      that.operations.entity.menuId = menu.id
      that.operations.entity.menuName = menu.name
      that.operations.entity.menuGovCode = menu.govCode
      this.ms.dialogVisible = false
    },
    onAddOperation() {
      const that = this
      that.$refs['operationForm'].validate((valid) => {
        if (valid) {
          const operation = Object.assign({ dataType: 'Object' }, that.operations.entity)
          saveOperation(operation).then((response) => {
            const tempData = Object.assign({}, response.data)
            this.operations.list.unshift(tempData)
            this.operations.dialogVisible = false
            this.$notify({ title: '提示', message: '创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    toEditOperaton(row) {
      const that = this
      that.operations.dialogVisible = true
      that.operations.dialogStatus = 'update'
      that.resetOperationEntity(row)
      that.$nextTick(() => {
        that.$refs['operationForm'].clearValidate()
      })
    },
    onUpdateOperation() {
      const that = this
      that.$refs['operationForm'].validate((valid) => {
        if (valid) {
          const operation = Object.assign({ dataType: 'Object' }, that.operations.entity)
          saveOperation(operation).then(() => {
            const index = that.operations.list.findIndex(v => v.id === operation.id)
            that.operations.list.splice(index, 1, that.operations.entity)
            that.operations.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
          })
        }
      })
    },
    onOperatonDelete(row) {
      const that = this
      that.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const { id, uuid } = row
        deleteOperation({ id, uuid }).then((response) => {
          const index = that.operations.list.indexOf(row)
          that.operations.list.splice(index, 1)
          that.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch(err => console.log(err))
      }).catch(err => console.log(err))
    },
    /* ICO */
    toggleShow() {
      this.show = true
    },
    /**
			 * crop success
			 *
			 * [param] imgDataUrl
			 * [param] field
			 */
    cropSuccess(imgDataUrl, field) {
      console.log('-------- crop success --------')
      this.app.ico = imgDataUrl
    },
    /**
			 * upload success
			 *
			 * [param] jsonData   服务器返回数据，已进行json转码
			 * [param] field
			 */
    cropUploadSuccess(jsonData, field) {
      const that = this
      console.log('-------- upload success --------')
      console.log(jsonData)
      console.log('field: ' + field)
      if (jsonData && jsonData.code === '0') {
        const req = { id: that.app.id, uuid: that.app.uuid, type: jsonData.data.path }
        saveAppIco(req).then(() => {
          that.show = false
          that.$notify({ title: '提示', message: '保存成功！', type: 'success', duration: 2000 })
        }).catch(() => {
        })
      }
    },
    /**
			 * upload fail
			 *
			 * [param] status    server api return error status, like 500
			 * [param] field
			 */
    cropUploadFail(status, field) {
      console.log('-------- upload fail --------')
      console.log(status)
      console.log('field: ' + field)
    }

  }
}
</script>
