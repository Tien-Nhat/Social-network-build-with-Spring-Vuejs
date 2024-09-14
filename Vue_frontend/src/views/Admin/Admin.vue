<template>
    <div class="container">
        <nav id="sidebarMenu" class="collapse d-lg-block sidebar collapse bg-white">
            <div class="position-sticky">
                <div class="list-group list-group-flush mx-3 mt-4">
                    <a @click="currentTab = 0, getListUser()" style="cursor: pointer"
                        :class="'list-group-item list-group-item-action py-2 ripple ' + ((currentTab == 0) ? 'active' : '')"><i
                            class="fas fa-users fa-fw me-3"></i><span>Users</span></a>
                    <a @click="openReportPage()" style="cursor: pointer"
                        :class="'list-group-item list-group-item-action py-2 ripple ' + ((currentTab == 1) ? 'active' : '')"><i
                            class="fa fa-pie-chart fa-fw me-3"></i><span>Report</span></a>
                </div>
            </div>
        </nav>
        <div v-if="currentTab == 0" class="container-table">
            <div style="text-align: center">
                <h2>List User</h2>
            </div>
            <el-input @keydown.enter.exact.prevent="searchUserBy" v-model="txtSearch" style="max-width: 400px"
                placeholder="Text search here" class="input-with-select">
                <template #prepend>
                    <el-button @click="searchUserBy"><el-icon>
                            <Search />
                        </el-icon></el-button>
                </template>
                <template #append>
                    <el-select v-model="select" placeholder="Search by" style="width: 115px">
                        <el-option label="All" value="" />
                        <el-option label="Username" value="username" />
                        <el-option label="First Name" value="firstname" />
                        <el-option label="Last Name" value="lastname" />
                        <el-option label="Email" value="email" />
                    </el-select>
                </template>
            </el-input>
            <el-table :data="listUser.content" border style="width: 100%; margin-top: 10px;">
                <el-table-column label="Username" prop="username" />
                <el-table-column label="First name" prop="firstName" />
                <el-table-column label="Last name" prop="lastName" sortable />
                <el-table-column label="Email" prop="email" />
                <el-table-column label="Phone" prop="phone" />
                <el-table-column label="Address" prop="address" />
                <el-table-column label="Operation" align="center">
                    <template #default="scope">
                        <el-button size="small" v-if="!scope.row.banned"
                            @click="banUser(scope.row.userId, scope.$index)" type="danger">
                            <Close style="width: 1em; height: 1em; margin-right: 8px" />Block
                        </el-button>
                        <el-button size="small" v-else @click="banUser(scope.row.userId, scope.$index)" type="success">
                            <Check style="width: 1em; height: 1em; margin-right: 8px" />Unblock
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="d-flex justify-content-center">
                <el-pagination style="margin-left: auto;margin-right: auto;" layout="prev, pager, next"
                    :total="this.listUser.totalElements" :page-size="pageSize" :hide-on-single-page="true"
                    @current-change="changePage"></el-pagination>
            </div>

        </div>

        <div v-if="currentTab == 1" class="container-table" style="text-align: center">
            <h2>List Report</h2>
            <el-table :data="pagedTableData" border table-layout="fixed">
                <el-table-column prop="reporterUsername" label="Reporter Username" width="140" />
                <el-table-column prop="reportedUsername" label="Reported Username" width="140" />
                <el-table-column label="Link refer" width="120">
                    <template #default="scope">
                        <a :href="'/post/' + scope.row.postId" target="_blank" class="small text-body text-opacity-70">
                            Link
                        </a>
                    </template>
                </el-table-column>
                <el-table-column prop="reportType" label="Type" width="250" sortable />
                <el-table-column prop="reason" label="Reason" />
                <el-table-column :filters="[
                    { text: 'Resolved', value: 'Resolved' },
                    { text: 'Open', value: 'Open' },
                    { text: 'In progressing', value: 'In_progressing' },
                ]" :filter-method="filterTag" label="Status" width="140">
                    <template #default="scope">
                        <el-tag :type="typeStatus(scope.row.status)">
                            {{ scope.row.status }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="Block post" width="120">
                    <template #default="scope">
                        <el-button size="small" v-if="!scope.row.postDeleted" :disabled="isResolved(scope.row.status)"
                            @click="deletePost(scope.row.postId, scope.$index)" type="danger">
                            <Close style="width: 1em; height: 1em; margin-right: 8px" />Block
                        </el-button>
                        <el-button size="small" v-else :disabled="isResolved(scope.row.status)"
                            @click="restorePost(scope.row.postId, scope.$index)" type="success">
                            <Check style="width: 1em; height: 1em; margin-right: 8px" />Unblock
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column label="Block user" width="120">
                    <template #default="scope">
                        <el-button size="small" v-if="!scope.row.userBaned" :disabled="isResolved(scope.row.status)"
                            @click="banUser(scope.row.reportedUserId, scope.$index)" type="danger">
                            <User style="width: 1em; height: 1em; margin-right: 8px" />Block
                        </el-button>
                        <el-button size="small" v-else :disabled="isResolved(scope.row.status)"
                            @click="banUser(scope.row.reportedUserId, scope.$index)" type="success">
                            <User style="width: 1em; height: 1em; margin-right: 8px" />Unblock
                        </el-button>
                    </template>
                </el-table-column>
                <el-table-column label="Mark as done" width="140">
                    <template #default="scope">
                        <el-select v-model="scope.row.status" placeholder="Select" size="small" style="width: 100px">
                            <el-option v-for="item in status"
                                @click="changeStatus(scope.row.reportId, scope.row.status)" :key="item.value"
                                :label="item.label" :value="item.value" />
                        </el-select>
                    </template>
                </el-table-column>
            </el-table>
            <div class="d-flex justify-content-center">
                <el-pagination style="margin-left: auto;margin-right: auto;" layout="prev, pager, next"
                    :total="this.listReport.length" :page-size="pageSizeReport" :hide-on-single-page="true"
                    @current-change="setPageReport"></el-pagination>
            </div>

        </div>

    </div>


</template>
<script>
import "https://code.jquery.com/jquery-1.10.2.min.js";
import adminService from "@/services/admin.service";
import postService from "@/services/post.service";

export default {
    name: 'TimeLine',
    data() {
        return {
            select: "",
            listUser: {},
            listReport: [],
            response: {
                status: "",
                message: "",
                result: null
            },
            currentPage: 0,
            pageSize: 10,
            txtSearch: "",
            initialUser: {},
            statusSelect: "Open",
            status: [
                {
                    value: "Open",
                    label: "Open",
                },
                {
                    value: "In_progressing",
                    label: 'In progressing',
                },
                {
                    value: "Resolved",
                    label: "Resolved",
                }
            ],
            dirty: false,
            currentTab: 0,
            pageReport: 1,
            pageSizeReport: 10,
            cloneListReport: {},
            unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
        }
    },
    computed: {
        pagedTableData() {
            return this.listReport.slice(this.pageSizeReport * this.pageReport - this.pageSizeReport, this.pageSizeReport * this.pageReport)
        }
    },
    mounted() {
        this.getListUser();
    },
    methods: {
        typeStatus(status) {

            switch (status) {
                case "OPEN":
                    return "info";
                case "IN_PROGRESSING":
                    return "primary";
                default:
                    return "success";
            }
        },
        filterTag(value, row) {
            return row.status === value
        },
        setPageReport(val) {
            this.pageReport = val
        },
        isResolved(status) {
            return (status == "Resolved");
        },
        async changeStatus(reportId, status) {
            await adminService.changeStatus(reportId, status).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                }
            );
            if (this.response.status == "SUCCESS")
                this.listReport[this.foundReportIndex(reportId)].status = this.response.result;
        },
        foundReportIndex(reportId) {
            return this.listReport.findIndex(report => report.reportId === reportId);
        },
        async searchUserBy() {
            if (this.select != "") {
                await adminService.searchUserBy(this.select, this.txtSearch, this.currentPage, this.pageSize).then(
                    (response) => {
                        this.response = response.data;
                    },
                    (error) => {
                        this.response =
                            (error.response &&
                                error.response.data &&
                                error.response.data.message) ||
                            error.message ||
                            error.toString();

                    }
                );
                if (this.response.status == "SUCCESS")
                    this.listUser = this.response.result;
            } else this.getListUser();
        },
        changePage(val) {
            this.currentPage = val - 1;
            if (this.txtSearch.length > 0)
                this.searchUserBy();
            else
                this.getListUser();
        },
        async getListUser() {
            await adminService.getListUser(this.currentPage, this.pageSize).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                }
            );
            if (this.response.status == "SUCCESS")
                this.listUser = this.response.result;

        },
        async banUser(userId, index) {
            await adminService.banUser(userId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                }
            );
            if (this.response.status == "SUCCESS") {

                this.listUser.content[index].banned = this.response.result;
                this.listReport[index].userBaned = this.response.result;
            }
        },
        async getListReport() {
            await adminService.getListReport().then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                }
            );
            if (this.response.status == "SUCCESS")

                this.listReport = this.response.result;

        },
        openReportPage() {
            this.currentTab = 1;
            this.currentPage = 0;
            this.getListReport();
        },
        async deletePost(postId, index) {
            await postService.deletePost(postId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                }
            );
            if (this.response.status == "SUCCESS") {
                this.listReport[index].postDeleted = true;

            }

        },
        async restorePost(postId, index) {
            await postService.restorePost(postId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();
                }
            );
            if (this.response.status == "SUCCESS") {
                this.listReport[index].postDeleted = false;
            }

        },
    },
}
</script>
<style scoped>
@import url(./Admin.css);
</style>
