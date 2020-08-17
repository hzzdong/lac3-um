<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" :tab-position="tabPosition" style="height: 100%;" @tab-click="handleTabClick">
      <el-tab-pane label="单位基本信息" name="tabCompany">
        <div class="filter-container tool-bar">
          <el-button type="primary" icon="el-icon-close" circle plain @click="onClose()" />
          <el-button type="primary" icon="el-icon-refresh" circle plain @click="onRefresh()" />
          <el-button v-permission="['kh_company_m']" type="success" icon="el-icon-edit" circle @click="toCustomerUpdate()" />
          <el-button
            v-if="company.status === 0 && checkPermission(['kh_company_m']) === true"
            type="warning"
            icon="el-icon-lock"
            title="禁用"
            circle
            @click="toChangeStatus(1)"
          />
          <el-button
            v-if="company.status === 1 && checkPermission(['kh_company_m']) === true"
            type="warning"
            icon="el-icon-unlock"
            title="启用"
            circle
            @click="toChangeStatus(0)"
          />
          <el-button v-permission="['kh_company_m']" type="danger" icon="el-icon-delete" circle @click="onCustomerDelete()" />
        </div>
        <el-form ref="companyForm" :model="company" size="small" status-icon label-position="right" label-width="120px" style="width: 100%;">
          <el-card class="box-card" style="margin-top: -10px; margin-bottom: 10px;">
            <div slot="header" class="clearfix">
              <span>单位基本信息</span>
              <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="单位名称:" prop="name">
                    <span class="el-span_view">{{ company.name }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="单位编码:" prop="govCode">
                    <span class="el-span_view">{{ company.govCode }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="电话:" prop="phone">
                    <span class="el-span_view">{{ company.phone }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="传真:" prop="fax">
                    <span class="el-span_view">{{ company.fax }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="所在区域:" prop="areaName">
                    <span class="el-span_view">{{ company.areaName }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="单位网址:" prop="url">
                    <span class="el-span_view">{{ company.url }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="排序号:" prop="sort">
                    <span class="el-span_view">{{ company.sort }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="状态:" prop="status">
                    <span class="el-span_view">{{ company.status | statusFilter }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="单位地址:" prop="address">
                    <span class="el-span_view">{{ company.address }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="company.typeCode === 'kh_qy'">
                <el-col :span="12">
                  <el-form-item label="单位规模:" prop="scale">
                    <span class="el-span_view">{{ company.scale }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="业务开通:" prop="businessStart">
                    <span class="el-span_view">{{ company.businessStart }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="company.typeCode === 'kh_qy'">
                <el-col :span="12">
                  <el-form-item label="证照类型:" prop="certificateType">
                    <span class="el-span_view">{{ company.certificateType | certificateTypeFilter }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="证照号码:" prop="certificateNo">
                    <span class="el-span_view">{{ company.certificateNo }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="company.typeCode === 'kh_qy'">
                <el-col :span="24">
                  <el-form-item label="经营范围:" prop="business">
                    <el-input v-model="company.business" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="经营范围" class="el-textarea_view" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row v-if="company.typeCode === 'kh_qy'">
                <el-col :span="24">
                  <el-form-item label="单位资质:" prop="credentials">
                    <el-input v-model="company.credentials" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="单位资质" class="el-textarea_view" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="创建时间:" prop="createTime">
                    <span class="el-span_view">{{ company.createTime }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="最后更新:" prop="updateTime">
                    <span class="el-span_view">{{ company.updateTime }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="备注:">
                    <el-input v-model="company.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" class="el-textarea_view" />
                  </el-form-item>
                </el-col>
              </el-row>
            </div></el-card>
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>单位联系人信息</span>
              <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
            </div>
            <div class="text item">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="联系人/法人:" prop="juridical">
                    <span class="el-span_view">{{ company.juridical }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="联系电话:" prop="jphone">
                    <span class="el-span_view">{{ company.jphone }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="证照类型:" prop="jType">
                    <span class="el-span_view">{{ company.jType | personCertificateTypeFilter }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="证照号码:" prop="jNo">
                    <span class="el-span_view">{{ company.jNo }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="单位机构树预览" name="tabCompanyTreeView">
        <el-table
          ref="companyTreeViewTable"
          :data="companyTree"
          style="width: 100%;margin-bottom: 20px;"
          row-key="id"
          border
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        >
          <el-table-column prop="name" label="名称" width="250">
            <template slot-scope="{row}">
              <span>
                <i v-if="row.attributes.alias==='Company'" class="el-icon-office-building" title="单位" />
                <i v-if="row.attributes.alias!=='Company' && row.attributes.mdep !== 1" class="el-icon-user" title="部门" />
                <i v-if="row.attributes.alias!=='Company' && row.attributes.mdep === 1" class="el-icon-user-solid" title="管理部门" />
                {{ row.name }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="govCode" label="编号" width="150" />
          <el-table-column label="联系人" width="120">
            <template slot-scope="{row}">
              <span>{{ row.attributes.linkUserName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="联系方式" width="110">
            <template slot-scope="{row}">
              <span>{{ row.attributes.linkUserPhone }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="类型" width="80">
            <template slot-scope="{row}">
              <el-tag v-if="row.type!==null && row.type>0" size="small">{{ row.type | orgTypeFilter }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="60" />
          <el-table-column prop="remark" label="备注说明" min-width="100" />
          <el-table-column label="" class-name="status-col" width="40" prop="status">
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="单位许可应用" name="tabCompanyApps">
        <aside>
          <i class="el-icon-info" /> 单位[ <span>{{ company.name }}</span> ]已许可应用列表。您可以在此查看或给客户单位许可应用，配置应用权限。
        </aside>
        <el-row>
          <el-col :span="12">
            <div v-if="checkPermission(['kh_company_app']) === true" class="filter-container">
              <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="toSelectApps()">
                添加许可
              </el-button>
            </div>
            <el-table
              :key="companyApps.tableKey"
              ref="companyAppTable"
              v-loading="companyApps.listLoading"
              :data="companyApps.list"
              tooltip-effect="dark"
              style="width: 100%"
              border
              fit
              highlight-current-row
              @current-change="handleAppSelectedChange"
            >
              <el-table-column prop="status" label="" class-name="status-col" width="40">
                <template slot-scope="{row}">
                  <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
                </template>
              </el-table-column>
              <el-table-column label="应用名称" min-width="150px" prop="name">
                <template slot-scope="{row}">
                  <span>{{ row.name }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="code" label="编号" width="200px" />
              <el-table-column v-if="checkPermission(['kh_company_app']) === true" label="操作" align="center" width="50" class-name="small-padding">
                <template slot-scope="{row}">
                  <el-button type="danger" size="mini" icon="el-icon-close" title="移除" @click="onCompanyAppDelete(row)" />
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="companyApps.total>0" :total="companyApps.total" :page.sync="companyApps.listQuery.page" :limit.sync="companyApps.listQuery.limit" @pagination="getCompanyApps" />
          </el-col>
          <el-col :span="12" style="padding-left: 10px;">
            <div v-if="checkPermission(['kh_company_app']) === true" class="filter-container">
              <el-button class="filter-item" type="primary" icon="el-icon-circle-check" @click="onSaveCompanyAppPermission()">
                保存应用权限配置
              </el-button>
            </div>
            <el-tabs type="border-card">
              <el-tab-pane label="菜单权限">
                <el-tree
                  ref="tree_menu"
                  node-key="id"
                  show-checkbox
                  :data="menuTree.data"
                  :props="menuTree.defaultProps"
                  :default-expand-all="true"
                  :expand-on-click-node="false"
                  :check-on-click-node="true"
                  :check-strictly="true"
                />
              </el-tab-pane>
            </el-tabs>
            <div v-if="checkPermission(['kh_company_app']) === true" class="tabs-toolbar" title="切换全选状态">
              <el-checkbox v-model="checkAll" @change="triggerMenuTreeCheck" /> 全选
            </div>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="单位用户列表" name="tabCompanyUsers">
        <aside>
          <i class="el-icon-info" /> 单位[ <span>{{ company.name }}</span> ]用户列表列表。
        </aside>
        <div class="filter-container">
          <el-input v-model="companyUsers.listQuery.rules.name.fv" placeholder="姓名 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="queryCompanyUsers" />
          <el-input v-model="companyUsers.listQuery.rules.mobile.fv" placeholder="手机号 模糊匹配" style="width: 150px;" class="filter-item" />
          <el-select v-model="companyUsers.listQuery.rules.status.fv" placeholder="状态" clearable class="filter-item" style="width: 130px">
            <el-option v-for="item in companyUsers.statusOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
          <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="queryCompanyUsers" />
          <el-button
            class="filter-item"
            style="float: right;"
            type="primary"
            icon="el-icon-plus"
            @click="toCreateUser()"
          >
            添加
          </el-button>
        </div>
        <el-table
          ref="userTable"
          :data="companyUsers.list"
          tooltip-effect="dark"
          border
          style="width: 100%"
        >
          <el-table-column label="" class-name="status-col" width="40" prop="status" sortable>
            <template slot-scope="{row}">
              <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
            </template>
          </el-table-column>
          <el-table-column label="姓名" width="180px" prop="name" sortable>
            <template slot-scope="{row}">
              <span v-if="checkPermission(['kh_company_m']) === false">{{ row.name }}</span>
              <router-link v-if="checkPermission(['kh_company_m']) === true" :to="'/customer/user-view/'+row.id+'/'+row.uuid" class="link-type">
                <span>{{ row.name }}</span>
              </router-link>
              <el-tag v-if="row.type == 9">管</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="账号" width="150px" align="center" prop="account" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.account }}</span>
            </template>
          </el-table-column>
          <el-table-column label="部门" width="250px" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.orgFullName || scope.row.companyName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="手机" width="120px" align="center" prop="mobile" sortable>
            <template slot-scope="scope">
              <span>{{ scope.row.mobile }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注说明" min-width="100px" />
        </el-table>
        <pagination v-show="companyUsers.total>0" :total="companyUsers.total" :page.sync="companyUsers.listQuery.page" :limit.sync="companyUsers.listQuery.limit" @pagination="getCompanyUsers" />
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="单位可许可应用选择" :visible.sync="apps.dialogVisible" width="75%">
      <aside>
        <i class="el-icon-info" /> 单位[ <span>{{ company.name }}</span> ]可许可应用列表。选择应用后点击“确定选择”按钮即可给单位许可选中应用。
      </aside>
      <div class="filter-container">
        <el-input v-model="apps.listQuery.rules.name.fv" placeholder="名称 模糊匹配" style="width: 150px;" class="filter-item" @keyup.enter.native="queryUnCompanyApps" />
        <el-input v-model="apps.listQuery.rules.code.fv" placeholder="编号 精确匹配" style="width: 150px;" class="filter-item" />
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="queryUnCompanyApps()" />
        <el-button class="filter-item" style="float: right;" type="primary" icon="el-icon-check" @click="onSelectApps()">
          确定选择
        </el-button>
      </div>
      <el-table
        ref="unCompanyAppTable"
        :data="apps.list"
        tooltip-effect="dark"
        border
        style="width: 100%"
        @selection-change="handleSelectAppChange"
        @row-click="handleAppRowClick"
      >
        <el-table-column type="selection" width="40" />
        <el-table-column label="" class-name="status-col" width="40" prop="status" sortable>
          <template slot-scope="{row}">
            <el-tag effect="dark" size="small" :type="row.status | statusTypeFilter" :title="row.status | statusFilter" />
          </template>
        </el-table-column>
        <el-table-column label="应用名称" width="250px" prop="name" sortable>
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="编号" width="200px" prop="code" sortable>
          <template slot-scope="scope">
            <span>{{ scope.row.code }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注说明" min-width="100px" />
      </el-table>
      <pagination v-show="apps.total>0" :total="apps.total" :page.sync="apps.listQuery.page" :limit.sync="apps.listQuery.limit" @pagination="findUnCompanyApps" />
    </el-dialog>

    <el-dialog title="编辑客户单位" :visible.sync="dialogVisible" width="70%">
      <el-form ref="companyForm" :rules="rules" :inline="false" :model="entity" size="small" status-icon label-position="right" label-width="120px" style="width: 90%; margin-left:30px;">
        <el-card class="box-card" style="margin-top: -20px; margin-bottom: 10px;">
          <div slot="header" class="clearfix">
            <span>单位基本信息</span>
            <el-radio-group v-model="entity.typeCode" style="float: right;" size="small">
              <el-radio-button v-for="item in commonData.companyClasses" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
            </el-radio-group>
          </div>
          <div class="text item">
            <el-row>
              <el-col :span="12">
                <el-form-item label="单位名称" prop="name">
                  <el-input v-model="entity.name" placeholder="请输入单位名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="编号" prop="govCode">
                  <el-input v-model="entity.govCode" placeholder="请输入单位编号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="电话" prop="phone">
                  <el-input v-model="entity.phone" placeholder="请输入单位联系电话" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="传真" prop="fax">
                  <el-input v-model="entity.fax" placeholder="请输入单位传真号码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="所在区域" prop="areaName">
                  <el-input v-model="entity.areaName" :disabled="true" placeholder="请选择所在区域">
                    <el-popover
                      slot="append"
                      placement="bottom"
                      title="区域选择"
                      width="500"
                      trigger="click"
                    >
                      <div style="height:500px;overflow-y:auto">
                        <el-tree
                          ref="areaTree"
                          :data="areaTree.data"
                          :props="areaTree.defaultProps"
                          node-key="id"
                          :expand-on-click-node="false"
                          :check-on-click-node="true"
                          show-checkbox
                          :check-strictly="true"
                          @node-click="handleNodeClick"
                          @check-change="handleCheckChange"
                        />
                      </div>
                      <el-button slot="reference" icon="el-icon-search" />
                    </el-popover>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="单位网址" prop="url">
                  <el-input v-model="entity.url" placeholder="请输入单位网址" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="排序号" prop="sort">
                  <el-input v-model.number="entity.sort" placeholder="请输入排序号" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="entity.status" size="small">
                    <el-radio-button v-for="item in statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="单位地址" prop="address">
                  <el-input v-model.number="entity.address" placeholder="请输入单位地址" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="entity.typeCode === 'kh_qy'">
              <el-col :span="12">
                <el-form-item label="单位规模" prop="scale">
                  <el-input v-model="entity.scale" placeholder="请输入人员规模" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务开通" prop="businessStart">
                  <el-date-picker v-model="entity.businessStart" type="datetime" placeholder="请选择" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="entity.typeCode === 'kh_qy'">
              <el-col :span="12">
                <el-form-item label="证照类型" prop="certificateType">
                  <el-select v-model="entity.certificateType" class="filter-item" placeholder="请选择" style="width:100%;">
                    <el-option v-for="item in commonData.certificateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="证照号码" prop="certificateNo">
                  <el-input v-model="entity.certificateNo" placeholder="请输入证照号码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="entity.typeCode === 'kh_qy'">
              <el-col :span="24">
                <el-form-item label="经营范围" prop="business">
                  <el-input v-model="entity.business" :autosize="{ minRows: 2, maxRows: 5}" type="textarea" placeholder="请输入经营范围" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="entity.typeCode === 'kh_qy'">
              <el-col :span="24">
                <el-form-item label="单位资质" prop="credentials">
                  <el-input v-model="entity.credentials" :autosize="{ minRows: 2, maxRows: 5}" type="textarea" placeholder="请输入经营范围" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="备注" prop="remark">
                  <el-input v-model="entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>单位联系人信息</span>
            <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
          </div>
          <div class="text item">
            <el-row>
              <el-col :span="12">
                <el-form-item label="联系人/法人" prop="juridical">
                  <el-input v-model="entity.juridical" placeholder="请输入联系人或法人名称" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="jphone">
                  <el-input v-model="entity.jphone" placeholder="请输入联系人或法人联系电话" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="证照类型" prop="jType">
                  <el-select v-model="entity.jType" class="filter-item" placeholder="请选择" style="width:100%;">
                    <el-option v-for="item in commonData.personCertificateTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="证照号码" prop="jNo">
                  <el-input v-model="entity.jNo" placeholder="请输入证照号码" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onCustomerUpdate()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="companyUsers.textMap[companyUsers.dialogStatus]" :visible.sync="companyUsers.dialogVisible" width="75%">
      <el-form ref="userForm" :rules="companyUsers.rules" :inline="false" :model="companyUsers.entity" size="small" status-icon label-position="right" label-width="80px" style="width: 98%; margin-left:10px;">
        <el-card class="box-card" style="margin-top: -20px;">
          <div slot="header" class="clearfix">
            <span>用户基本信息</span>
            <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-warning-outline" />
          </div>
          <div class="text item">
            <el-row>
              <el-col :span="12">
                <el-form-item label="所属机构" prop="orgName">
                  <el-input v-model="companyUsers.entity.orgName" :disabled="true">
                    <el-button slot="append" icon="el-icon-search" @click="toSelectOrg()" />
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" prop="status">
                  <el-radio-group v-model="companyUsers.entity.status" size="small">
                    <el-radio-button v-for="item in companyUsers.statusOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="companyUsers.entity.name" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机" prop="mobile">
                  <el-input v-model="companyUsers.entity.mobile" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="账号" prop="account">
                  <el-input v-model="companyUsers.entity.account" :disabled="companyUsers.entity.id != undefined" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="工号" prop="govCode">
                  <el-input v-model="companyUsers.entity.govCode" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="密码" prop="password">
                  <el-input v-model="companyUsers.entity.password" type="password" autocomplete="off" placeholder="请输入密码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="确认密码" prop="checkPass">
                  <el-input v-model="companyUsers.entity.checkPass" type="password" autocomplete="off" placeholder="请输入确认密码" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="用户类型" prop="type">
                  <el-radio-group v-model="companyUsers.entity.type" size="small">
                    <el-radio-button v-for="item in companyUsers.typeOptions" :key="item.key" :label="item.key">{{ item.display_name }}</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="昵称" prop="nickName">
                  <el-input v-model="companyUsers.entity.nickName" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="备注">
                  <el-input v-model="companyUsers.entity.remark" :autosize="{ minRows: 3, maxRows: 5}" type="textarea" placeholder="请输入备注说明" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="companyUsers.dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="onCreateUser()">保存</el-button>
      </div>
    </el-dialog>

    <el-dialog id="dlg-org-select4user-view" title="机构选择" :visible.sync="os.dialogVisible" width="40%">
      <lac-org-single-select
        :tree-type="os.treeType"
        :company-id="os.companyId"
        :company-uuid="os.companyUuid"
        @onOrgSingleSelected="onOrgSelected"
      />
    </el-dialog>

  </div>
</template>

<script>
import { fetchKhCompany,
  saveKhCompany,
  deleteKhCompany,
  getFullOrgTree,
  addCompanyApps,
  removeCompanyApps,
  getComapnyPermedMenuTree,
  saveCompanyAppMenuPerm,
  changeCompanyStatus
} from '@/api/customer'
import { getPage, createUser } from '@/api/khuser'
import { findAppPage4KhCompany, findAppPage4UnKhCompany } from '@/api/application'
import { loadTree } from '@/api/area'
import { sheetClose, sheetRefresh, parseCheckedTreeIds, checkTree, unCheckTree } from '@/utils'
import { loadOrgCertificateType, loadPersonCertificateType, loadOrgType, loadCompanyPositions } from '@/utils/laccache'
import md5 from 'js-md5'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { statusOptions, userTypeOptions, appTypeOptions, companyClasses } from '@/filters'
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数
import { validateMobile } from '@/utils/validate'
import LacOrgSingleSelect from '@/views/customer/components/OrgSingleSelect'

const commonData = {
  companyClasses: companyClasses(),
  certificateTypeOptions: [],
  personCertificateTypeOptions: [],
  orgTypeOptions: [],
  companyPositions: []
}

export default {
  name: 'CustomerView',
  components: { Pagination, LacOrgSingleSelect },
  directives: { waves, permission },
  filters: {
    orgTypeFilter(type) {
      const otype = type + ''
      if (commonData.orgTypeOptions && commonData.orgTypeOptions.length > 0) {
        for (const ct of commonData.orgTypeOptions) {
          if (ct.key === otype) {
            return ct.display_name
          }
        }
      }
      return ''
    },
    certificateTypeFilter(type) {
      if (commonData.certificateTypeOptions && commonData.certificateTypeOptions.length > 0) {
        for (const ct of commonData.certificateTypeOptions) {
          if (ct.key === type) {
            return ct.display_name
          }
        }
      }
      return ''
    },
    personCertificateTypeFilter(type) {
      if (commonData.personCertificateTypeOptions && commonData.personCertificateTypeOptions.length > 0) {
        for (const ct of commonData.personCertificateTypeOptions) {
          if (ct.key === type) {
            return ct.display_name
          }
        }
      }
      return ''
    },
    positionFilter(type) {
      if (commonData.companyPositions && commonData.companyPositions.length > 0) {
        for (const ct of commonData.companyPositions) {
          if (ct.key === type) {
            return ct.display_name
          }
        }
      }
      return ''
    }
  },
  data() {
    var validatePass = (rule, value, callback) => {
      if (this.companyUsers.entity.id) {
        if (value !== '' && this.companyUsers.entity.checkPass !== '') {
          this.$refs.userForm.validateField('checkPass')
        }
        callback()
      } else {
        if (value === '') {
          callback(new Error('请输入密码'))
        } else {
          if (this.companyUsers.entity.checkPass !== '') {
            this.$refs.userForm.validateField('checkPass')
          }
          callback()
        }
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (this.companyUsers.entity.id) {
        if (value !== '' && value !== this.companyUsers.entity.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      } else {
        if (value === '') {
          callback(new Error('请再次输入密码'))
        } else if (value !== this.companyUsers.entity.password) {
          callback(new Error('两次输入密码不一致!'))
        } else {
          callback()
        }
      }
    }
    return {
      commonData: { companyClasses: companyClasses() },
      tabPosition: 'right',
      activeTab: 'tabCompany',
      tempRoute: {},
      loading: false,
      statusOptions: statusOptions(),
      company: {},
      entity: {},
      dialogVisible: false,
      rules: {
        name: [{ required: true, message: '名称不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '名称长度在 2 到 64 个字符', trigger: 'blur' }],
        govCode: [{ required: true, message: '编号不能为空', trigger: 'blur' }, { min: 2, max: 64, message: '编号长度在 2 到 64 个字符', trigger: 'blur' }],
        phone: [{ min: 8, max: 32, message: '联系方式长度在 8 到 32 个字符', trigger: 'blur' }],
        fax: [{ min: 8, max: 32, message: '传真长度在 8 到 32 个字符', trigger: 'blur' }],
        areaName: [{ required: true, message: '区域不能为空', trigger: 'blur' }],
        url: [{ min: 10, max: 250, message: '联系方式长度在 10 到 250 个字符', trigger: 'blur' }],
        address: [{ min: 2, max: 120, message: '地址长度在 2 到 120 个字符', trigger: 'blur' }],
        scale: [{ type: 'number', message: '单位规模必须为数字值' }],
        certificateNo: [{ min: 2, max: 64, message: '证件号码长度在 2 到 64 个字符', trigger: 'blur' }],
        business: [{ min: 5, max: 500, message: '经营范围长度在 5 到 250 个字符', trigger: 'blur' }],
        credentials: [{ min: 2, max: 120, message: '单位资质长度在 2 到 120 个字符', trigger: 'blur' }],
        juridical: [{ min: 2, max: 60, message: '法人/联系人长度在 2 到 60 个字符', trigger: 'blur' }],
        jphone: [{ min: 2, max: 30, message: '联系方式长度在 2 到 30 个字符', trigger: 'blur' }],
        jNo: [{ min: 2, max: 60, message: '证照号码长度在 2 到 60 个字符', trigger: 'blur' }],
        sort: [{ required: true, message: '排序号不能为空', trigger: 'blur' }, { type: 'number', message: '排序号必须为数字值' }],
        remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
      },
      areaTree: {
        checkedId: undefined,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      companyTree: [],
      /* 应用许可 */
      companyApps: {
        loaded: false,
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            khCompanyId: { fv: undefined, oper: 'eq', stype: 'L' },
            khCompanyUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            code: { fv: undefined, oper: 'eq', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          }
        },
        statusOptions: statusOptions(),
        appTypeOptions: appTypeOptions(),
        dialogVisible: false
      },
      selectedApps: [],
      apps: {
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            khCompanyId: { fv: undefined, oper: 'eq', stype: 'L' },
            khCompanyUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            code: { fv: undefined, oper: 'eq', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        statusOptions: statusOptions(),
        appTypeOptions,
        dialogVisible: false
      },
      /* 应用授权 */
      currentApp: null,
      checkAll: false,
      menuTree: {
        loaded: false,
        data: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      },
      /* 单位用户 */
      companyUsers: {
        loaded: false,
        tableKey: 0,
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          rules: {
            companyId: { fv: undefined, oper: 'eq', stype: 'L' },
            companyUuid: { fv: undefined, oper: 'eq', stype: 'S' },
            name: { fv: undefined, oper: 'cn', stype: 'S' },
            mobile: { fv: undefined, oper: 'cn', stype: 'S' },
            status: { fv: undefined, oper: 'eq', stype: 'I' }
          },
          orderby: { orderby: 'id', order: 'desc' }
        },
        statusOptions: statusOptions(),
        typeOptions: userTypeOptions(),
        dialogStatus: '',
        textMap: {
          update: '用户编辑',
          create: '用户新增'
        },
        dialogVisible: false,
        entity: {},
        rules: {
          orgName: [{ required: true, message: '请选择归属机构', trigger: 'blur' }],
          account: [{ required: true, message: '账号不能为空', trigger: 'blur' }, { min: 6, max: 64, message: '账号长度在 2 到 64 个字符', trigger: 'blur' }],
          name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }, { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }],
          mobile: [{ validator: (rule, value, callback) => {
            if (value && value !== '') {
              if (!validateMobile(value)) {
                callback(new Error('请输入有效的手机号码'))
                return
              }
            }
            callback()
          }, trigger: 'blur' }],
          govCode: [{ min: 2, max: 64, message: '工号长度在 2 到 64 个字符', trigger: 'blur' }],
          timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
          password: [{ validator: validatePass, trigger: 'blur' }],
          checkPass: [{ validator: validatePass2, trigger: 'blur' }],
          remark: [{ max: 256, message: '备注说明长度不能大于 256 个字符', trigger: 'blur' }]
        }
      },
      os: {
        dialogVisible: false,
        treeType: 'SelfTree',
        companyId: 0,
        companyUuid: ''
      }

    }
  },
  created() {
    const id = this.$route.params && this.$route.params.id
    const uuid = this.$route.params && this.$route.params.uuid
    // Why need to make a copy of this.$route here?
    // Because if you enter this page and quickly switch tag, may be in the execution of the setTagsViewTitle function, this.$route is no longer pointing to the current page
    // https://github.com/PanJiaChen/vue-element-admin/issues/1221
    this.tempRoute = Object.assign({}, this.$route)

    this.os.companyId = Number.parseInt(id)
    this.os.companyUuid = uuid

    this.loadCommonData()
    this.fetchCompany(id, uuid)
    this.getAreaTree()
  },
  methods: {
    checkPermission,
    handleTabClick(tab, event) {
      if (tab.name === 'tabCompanyTreeView') {
        if (!this.companyTree || this.companyTree.length === 0) {
          this.getCompanyTree()
        }
      } else if (tab.name === 'tabCompanyApps') {
        if (!this.companyApps.list) {
          this.getCompanyApps()
        }
      } else if (tab.name === 'tabCompanyUsers') {
        if (!this.companyUsers.list) {
          this.getCompanyUsers()
        }
      }
    },
    loadCommonData() {
      loadOrgCertificateType().then(res => {
        commonData.certificateTypeOptions = (res && res.length > 0) ? res : []
        this.commonData.certificateTypeOptions = commonData.certificateTypeOptions
      }).catch((err) => console.log(err))

      loadPersonCertificateType().then(res => {
        commonData.personCertificateTypeOptions = (res && res.length > 0) ? res : []
        this.commonData.personCertificateTypeOptions = commonData.personCertificateTypeOptions
      }).catch((err) => console.log(err))

      loadOrgType().then(res => {
        commonData.orgTypeOptions = (res && res.length > 0) ? res : []
        this.commonData.orgTypeOptions = commonData.orgTypeOptions
      }).catch((err) => console.log(err))

      loadCompanyPositions().then(res => {
        commonData.companyPositions = (res && res.length > 0) ? res : []
        this.commonData.companyPositions = commonData.companyPositions
      }).catch((err) => console.log(err))
    },
    fetchCompany(id, uuid) {
      fetchKhCompany({ id, uuid }).then(response => {
        this.company = response.data
        this.setTagsViewTitle()
        this.setPageTitle()
      }).catch(err => console.log(err))
    },
    getCompanyTree() {
      getFullOrgTree({ id: this.company.id, uuid: this.company.uuid }).then(response => {
        this.companyTree = response.data
      })
    },
    getCompanyApps() {
      this.companyApps.listQuery.rules.khCompanyId.fv = this.company.id
      this.companyApps.listQuery.rules.khCompanyUuid.fv = this.company.uuid

      this.companyApps.listLoading = true
      findAppPage4KhCompany(this.companyApps.listQuery).then(response => {
        this.companyApps.list = response.data.data
        this.companyApps.total = response.data.recordsTotal
        this.companyApps.listLoading = false
        this.$nextTick(() => {
          if (this.companyApps.list && this.companyApps.list.length > 0) {
            this.currentApp = this.companyApps.list[0]
            this.$refs.companyAppTable.setCurrentRow(this.currentApp)
            // 加载权限
          }
        })
      }).catch(err => console.log(err))
    },
    setTagsViewTitle() {
      const title = '单位视图'
      const route = Object.assign({}, this.tempRoute, { title: `${title}-${this.company.name}` })
      this.$store.dispatch('tagsView/updateVisitedView', route)
    },
    setPageTitle() {
      const title = '单位视图'
      document.title = `${title} - ${this.company.name}`
    },
    onClose() {
      sheetClose(this)
    },
    onRefresh() {
      sheetRefresh(this)
    },
    getAreaTree() {
      /*
      loadCachedMyCompanyAreaTree().then(response => {
        this.areaTree.data = response
      }).catch((err) => console.log(err))
      */
      loadTree().then(response => {
        this.areaTree.data = response.data
      }).catch((err) => console.log(err))
    },
    handleNodeClick(data, checked, node) {
      this.areaTree.checkedId = data.id
      this.$refs.areaTree.setCheckedNodes([data])
    },
    handleCheckChange(data, checked, node) {
      if (checked === true) {
        this.areaTree.checkedId = data.id
        this.$refs.areaTree.setCheckedNodes([data])
        this.entity.areaId = data.id
        this.entity.areaName = data.name
      }
    },
    toChangeStatus(status) {
      this.$confirm('您确定要执行此操作吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { id: this.company.id, uuid: this.company.uuid, status: status }
        changeCompanyStatus(req).then(() => {
          this.company.status = status
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch((e) => console.log(e))
      }).catch((e) => console.log(e))
    },
    toCustomerUpdate() {
      const that = this
      that.entity = Object.assign({ dataType: 'Object' }, that.company)
      that.dialogVisible = true
      that.$nextTick(() => {
        that.$refs['companyForm'].clearValidate()
      })
    },
    onCustomerUpdate() {
      const that = this
      that.$refs['companyForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({ dataType: 'Object' }, that.entity)
          saveKhCompany(tempData).then(() => {
            that.company = Object.assign({}, that.entity)
            that.dialogVisible = false
            that.$notify({ title: '提示', message: '保存成功', type: 'success', duration: 2000 })
          })
        }
      })
    },
    onCustomerDelete() {
      const that = this
      that.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const { id, uuid } = that.company
        deleteKhCompany({ id, uuid }).then(() => {
          that.$notify({ title: '提示', message: '删除成功！', type: 'success', duration: 2000 })
          that.onClose()
        }).catch((err) => console.log(err))
      }).catch((err) => console.log(err))
    },
    /* 应用许可 */
    handleSelectAppChange(val) {
      this.selectedApps = val
    },
    handleAppRowClick(row, column, event) {
      this.$refs.unCompanyAppTable.toggleRowSelection(row)
    },
    findUnCompanyApps() {
      this.apps.listQuery.rules.khCompanyId.fv = this.company.id
      this.apps.listQuery.rules.khCompanyUuid.fv = this.company.uuid

      this.apps.listLoading = true
      findAppPage4UnKhCompany(this.apps.listQuery).then(response => {
        this.apps.list = response.data.data
        this.apps.total = response.data.recordsTotal
        this.apps.listLoading = false
      })
    },
    queryUnCompanyApps() {
      this.apps.listQuery.page = 1
      this.findUnCompanyApps()
    },
    toSelectApps() {
      this.findUnCompanyApps()
      this.apps.dialogVisible = true
    },
    onSelectApps() {
      if (!this.selectedApps || this.selectedApps.length <= 0) {
        this.$notify({ title: '提醒', message: '请先选择应用然后再操作！', type: 'error', duration: 4000 })
      } else {
        const req = { id: this.company.id, uuid: this.company.uuid, uuidIds: {}}
        for (let i = 0; i < this.selectedApps.length; i++) {
          const rowData = this.selectedApps[i]
          req.uuidIds[rowData.uuid] = rowData.id
        }

        addCompanyApps(req).then(() => {
          this.apps.dialogVisible = false
          for (let i = 0; i < this.selectedApps.length; i++) {
            this.companyApps.list.unshift(this.selectedApps[i])
          }
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch((err) => console.log(err))
      }
    },
    onCompanyAppDelete(row) {
      this.$confirm('您确定要删除吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const req = { parentId: this.company.id, parentUuid: this.company.uuid, id: row.id, uuid: row.uuid }
        removeCompanyApps(req).then(() => {
          const index = this.companyApps.list.indexOf(row)
          this.companyApps.list.splice(index, 1)
          this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
        }).catch((err) => console.log(err))
      }).catch((err) => console.log(err))
    },
    /* 应用授权*/
    handleAppSelectedChange(val) {
      this.currentApp = val
      this.loadMenuTree(this.company.id, this.company.uuid, this.currentApp.id, this.currentApp.uuid)
      this.menuTree.loaded = true
    },
    loadMenuTree(companyId, companyUuid, appId, appUuid) {
      const req = { parentId: companyId, parentUuid: companyUuid, id: appId, uuid: appUuid }
      getComapnyPermedMenuTree(req).then(response => {
        this.menuTree.data = response.data
        const checkedIds = []
        parseCheckedTreeIds(checkedIds, this.menuTree.data)
        this.$refs.tree_menu.setCheckedKeys(checkedIds)
      })
    },
    onSaveCompanyAppPermission() {
      const items = this.$refs.tree_menu.getCheckedNodes(false, true)
      console.log(items)
      const req = { parentId: this.company.id, parentUuid: this.company.uuid, id: this.currentApp.id, uuid: this.currentApp.uuid, uuidIds: {}}
      for (let i = 0; i < items.length; i++) {
        const item = items[i]
        req.uuidIds[item.uuid] = item.id
      }
      saveCompanyAppMenuPerm(req).then(() => {
        this.$notify({ title: '提示', message: '操作成功！', type: 'success', duration: 2000 })
      }).catch((err) => console.log(err))
    },
    triggerMenuTreeCheck() {
      if (this.checkAll) {
        checkTree(this.$refs.tree_menu, this.menuTree.data)
      } else {
        unCheckTree(this.$refs.tree_menu)
      }
    },
    /* 用户管理 */
    getCompanyUsers() {
      const that = this
      that.companyUsers.listQuery.rules.companyId.fv = that.company.id
      that.companyUsers.listQuery.rules.companyUuid.fv = that.company.uuid

      that.companyUsers.listLoading = true
      getPage(that.companyUsers.listQuery).then(response => {
        that.companyUsers.list = response.data.data
        that.companyUsers.total = response.data.recordsTotal
        that.companyUsers.listLoading = false
      }).catch(err => console.log(err))
    },
    queryCompanyUsers() {
      this.companyUsers.listQuery.page = 1
      this.getCompanyUsers()
    },
    toSelectOrg() {
      this.os.dialogVisible = true
    },
    onOrgSelected(org) {
      this.companyUsers.entity.parentUuid = org.uuid
      this.companyUsers.entity.parentName = org.name
      if (org.id > 0) {
        this.companyUsers.entity.parentId = org.id
        this.companyUsers.entity.parentClass = 'KhDepartment'
      } else {
        this.companyUsers.entity.parentId = org.id.substring(1)
        this.companyUsers.entity.parentClass = 'KhCompany'
      }
      this.os.dialogVisible = false
    },
    resetUser() {
      this.companyUsers.entity = {
        id: undefined,
        uuid: '',
        parentId: this.company.id,
        parentClass: 'KhCompany',
        orgName: this.company.name,
        name: '',
        account: '',
        govCode: '',
        mobile: '',
        password: '',
        checkPass: '',
        nickName: '',
        type: 1,
        status: 0,
        remark: '',
        roleEnabled: false
      }
    },
    toCreateUser() {
      this.resetUser()
      this.companyUsers.dialogStatus = 'create'
      this.companyUsers.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['userForm'].clearValidate()
      })
    },
    onCreateUser() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          const user = Object.assign({ dataType: 'Object' }, this.companyUsers.entity)
          user.password = md5(user.password)
          user.checkPass = ''
          user.roleEnabled = false
          createUser(user).then((response) => {
            const tempData = Object.assign({}, response.data)
            this.companyUsers.list.unshift(tempData)
            this.companyUsers.dialogVisible = false
            this.$notify({ title: '提示', message: '用户创建成功！', type: 'success', duration: 2000 })
          })
        }
      })
    }

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
    .tabs-toolbar {
      position: absolute;right:10px;top:65px;
      /* color: #e6a23c;
      font-weight: 600; */
      font-size: 14px;
    }
</style>
