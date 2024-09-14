<template>
    <main>
        <el-dialog :close-on-click-modal="false" :show-close="false" v-model="centerDialogVisible" title="Cảnh báo"
            width="500" center>
            <span>
                Phiên đăng nhập đã hết hạn hoặc có lỗi đăng nhập, vui lòng đăng nhập lại!
            </span>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="logOut">
                        Confirm
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <nav class="navbar navbar-expand-xl navbar-dark bg-dark">
            <a href="/" class="navbar-brand"><i class="fa fa-cube"></i>Pho<b>Ton</b></a>
            <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#navbarCollapse">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div id="navbarCollapse" class="collapse navbar-collapse justify-content-start">
                <form @keydown.enter.prevent="pushSearch" class="navbar-form form-inline">
                    <div class="dropdown">
                        <div class="input-group search-box" id="dropdownMenu2" data-toggle="dropdown"
                            aria-haspopup="false" aria-expanded="false">
                            <input v-model="txtSearch" @input="debouncedSearch" type="text" id="search"
                                class="form-control" placeholder="Search here...">
                            <button @click="pushSearch" type="submit" class="input-group-addon"><i
                                    class="material-icons">&#xE8B6;</i></button>
                        </div>
                        <div class="dropdown-menu dropdown-menu-lg-right" style="width: 350px;"
                            aria-labelledby="dropdownMenu2">
                            <a class="dropdown-item" v-for="(item, index) in listSuggestUser" :key="index"
                                :href="'/search?txtSearch=' + item" @click="txtSearch = item">
                                <span id="suggest">
                                    {{ item }}
                                </span>
                            </a>
                        </div>
                    </div>

                </form>
                <div class="navbar-nav ml-auto">
                    <a href="/" class="nav-item nav-link active"><i class="fa fa-home"></i><span>Home</span></a>
                    <a href="#" data-toggle="dropdown" data-target="#friend" class="nav-item nav-link"><i
                            class="fas fa-user-friends"></i>
                        <span v-if="countPendingFriend > 0" id="badgeFriend"
                            class="badge rounded-pill badge-notification bg-danger">
                            {{ countPendingFriend }}</span>
                        <span>Friend</span>
                        <div id="friend" class="dropdown">
                            <div class="dropdown-menu" style="width: 400px;" aria-labelledby="dropdownMenuButton">

                                <a class="dropdown-item" v-for="(item, index) in friendRequest" :key="item" href="#">

                                    <div class="row">
                                        <div class="col-md-2">
                                            <img :src="(item.avatar == null) ? unknowAvatar : 'data:image/*;base64,' + item.avatar"
                                                alt="" width="20" height="20" class="rounded-circle" />
                                        </div>
                                        <div class="col-md-10">
                                            <span class="highlight" style="line-break: auto">
                                                {{ item.firstName + " " + item.lastName + " " }}</span>
                                            <span class="more-info">{{ item.content }}</span>
                                        </div>
                                    </div>

                                    <div v-if="item.status == 'PENDING'" class="d-flex flex-row-reverse">
                                        <div class="p-2">
                                            <button @click="declineFriend(item.senderId)"
                                                class="btn btn-warning btn-icon-text btn-edit-profile">
                                                Decline
                                            </button>
                                        </div>
                                        <div class="p-2">
                                            <button @click="acceptFriend(item.senderId)"
                                                class="btn btn-success btn-icon-text btn-edit-profile">
                                                Accept
                                            </button>
                                        </div>

                                    </div>
                                </a>
                            </div>
                        </div>
                    </a>
                    <a href="#" data-toggle="dropdown" data-target="#notification" class="nav-item nav-link">
                        <i class="fa fa-bell"></i>
                        <span v-if="counNotRead > 0" id="badge" class="badge rounded-pill badge-notification bg-danger">
                            {{ counNotRead }}</span>
                        <span>Notifications</span>
                    </a>
                    <div id="notification" class="dropdown show" style="position: absolute;">
                        <div class="dropdown-menu" style="width: 400px;" aria-labelledby="dropdownMenuLink">
                            <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                                <a id="notifiItem" :href="'/post/' + item.postId" class="dropdown-item"
                                    v-for="(item, index) in notification" :key="item"
                                    @click="setIsRead(item.notifiId, index)"
                                    :style="(!item.isRead) ? 'color: black;' : ''">
                                    <div class="row">
                                        <div class="col-md-2">
                                            <img :src="(item.avatar == null) ? unknowAvatar : 'data:image/*;base64,' + item.avatar"
                                                alt="" width="20" height="20" class="rounded-circle" />
                                        </div>
                                        <div class="col-md-10">
                                            <span class="highlight" style="line-break: auto">
                                                {{ item.firstName + " " + item.lastName + " " }}</span>
                                            <span class="more-info">{{ item.notification }}</span>
                                        </div>
                                    </div>
                                    <small class="text-muted">{{ formatRelativeTime(item.notifiDate) }}</small>
                                </a>
                            </el-scrollbar>
                        </div>
                    </div>

                    <div class="nav-item dropdown">
                        <a :href="(isAdmin) ? '/admin' : ('/profile/' + getId())" class="btn btn-primary"></a>
                        <a href="#" data-toggle="dropdown" class="nav-item nav-link dropdown-toggle user-action"><img
                                :src="(user.avatar == null) ? unknowAvatar : 'data:image/*;base64,' + user.avatar"
                                class="avatar" alt="Avatar" @click="goProfile"> {{ user.firstName }}
                            {{ user.lastName }}
                            <b class="caret"></b></a>
                        <div class="dropdown-menu">
                            <a v-if="!isAdmin" :href="'/profile/' + getId()" class="dropdown-item"><i
                                    class="fa fa-user-o"></i>
                                Profile</a>
                            <a v-else href="/admin" class="dropdown-item"><i class="fas fa-user-tie"></i>
                                Admin Page</a>
                            <a href="/edit" class="dropdown-item"><i class="fa fa-sliders"></i> Change Info</a>
                            <div class="divider dropdown-divider"></div>
                            <a href="/login" @click="logOut" class="dropdown-item"><i
                                    class="material-icons">&#xE8AC;</i>
                                Logout</a>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    </main>
</template>
<script>
import 'https://code.jquery.com/jquery-3.5.1.min.js'
import userService from '@/services/user.service';
import notificationService from '@/services/notification.service';
import { ElNotification } from 'element-plus';
import friendService from '@/services/friend.service';
import debounce from 'debounce';

export default {
    name: 'Navbar',
    props: {
        test: {
            type: Number,
        }
    },
    data() {
        return {
            user: {
                firstName: "",
                lastName: "",
                avatar: ""
            },
            centerDialogVisible: false,
            response: {
                status: "",
                message: "",
                result: null
            },
            stompClient: null,
            txtSearch: "",
            initialNotifi: [],
            notification: [],
            friendRequest: [],
            counNotRead: null,
            countPendingFriend: null,
            listSuggestUser: [],
            unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
        }

    },
    computed: {
        isAdmin() {
            return this.$store.state.auth.user['roles'].includes('ROLE_ADMIN');
        },
    },

    mounted() {
        this.getUserInfo();
        this.getNotification();
        this.getFriendRequest();
    },
    methods: {
        debouncedSearch: debounce(function (event) {
            this.searchSuggestUser(event.target.value);
        }, 200),
        async searchSuggestUser(txtSearch) {
            await userService.searchSuggestUser(txtSearch).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            this.listSuggestUser = this.response.result;

        },
        addNotiFi(notifi) {
            var userNotifi = {};
            userNotifi = JSON.parse(notifi);
            ElNotification({
                title: 'Notification',
                dangerouslyUseHTMLString: true,
                message: '<img src="' + ((userNotifi.avatar == null) ? this.unknowAvatar : 'data:image/*;base64,' + userNotifi.avatar) + '" alt="" width="50" height="50" class="rounded-circle" /> <b>' + userNotifi.firstName + " " + userNotifi.lastName + " " + '</b>' + userNotifi.notification,
                icon: 'ChatSquare',
                position: "bottom-right"
            })
            this.notification.unshift(userNotifi);
            this.counNotRead = this.counNotRead + 1;
        },
        notifiFriend(notifi) {
            var friendNotifi = {};
            friendNotifi = JSON.parse(notifi);
            ElNotification({
                title: 'Notification',
                dangerouslyUseHTMLString: true,
                message: '<img src="' + ((friendNotifi.avatar == null) ? this.unknowAvatar : 'data:image/*;base64,' + friendNotifi.avatar) + '" alt="" width="50" height="50" class="rounded-circle" /> <b>' + friendNotifi.firstName + " " + friendNotifi.lastName + " " + '</b>' + friendNotifi.content,
                icon: 'User',
                position: "bottom-right"
            })

            if (friendNotifi.status == "NOT_FRIEND") {


                this.friendRequest[this.foundFriendIndex(friendNotifi.senderId)].content = friendNotifi.content;
                this.friendRequest[this.foundFriendIndex(friendNotifi.senderId)].status = friendNotifi.status;
            }
            else
                this.friendRequest.unshift(friendNotifi);

            if (friendNotifi.status == "PENDING")
                this.countPendingFriend = this.countPendingFriend + 1;
            else
                this.countPendingFriend = this.countPendingFriend - 1;

        },
        textCountLike(listLiked) {
            var nameFirstPerson = "";
            if (listLiked.length > 0)
                nameFirstPerson = listLiked[0].firstName + " " + listLiked[0].lastName

            switch (listLiked.length) {
                case 0:
                    return "";
                case 1:
                    return listLiked[0].firstName + " " + listLiked[0].lastName + " liked your post"
                case 2:
                    return nameFirstPerson + " and " + listLiked[1].firstName + " " + listLiked[1].lastName + " liked your post"
                case 3:
                    return nameFirstPerson + ", " + listLiked[1].firstName + " " + listLiked[1].lastName + " and " + listLiked[2].firstName + " " + listLiked[2].lastName + " liked your post"
                default:
                    return nameFirstPerson + ", " + listLiked[1].firstName + " " + listLiked[1].lastName + ", " + listLiked[2].firstName + " " + listLiked[2].lastName + " and " + (listLiked.length - 3) + " others liked your post";
            }
        },
        async declineFriend(friendId) {
            await friendService.deleteFriend(friendId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            if (this.response.status == "SUCCESS") {
                this.friendRequest[this.foundFriendIndex(friendId)].status = this.response.result;
                this.countPendingFriend = this.countPendingFriend - 1;
            }

            // else
            //     ElMessage.error(this.response.message);
        },
        async acceptFriend(senderId) {
            await friendService.acceptFriend(senderId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            if (this.response.status == "SUCCESS") {
                this.friendRequest[this.foundFriendIndex(senderId)].status = this.response.result;
                this.countPendingFriend = this.countPendingFriend - 1;
            }

            // else
            //     ElMessage.error(this.response.message);
        },
        async getFriendRequest() {
            await friendService.getFriendRequest().then(
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
                this.friendRequest = this.response.result.listFriendRe;
                this.countPendingFriend = this.response.result.countRequestPending;
            }

        },
        foundFriendIndex(senderId) {
            return this.friendRequest.findIndex(sender => sender.senderId === senderId);
        },
        foundNotifiPostIndex(postId) {
            return this.notification.findIndex(notifi => notifi.postId === postId);
        },
        async setIsRead(notifiId, index) {
            if (this.notification[index].isRead == false) {
                await notificationService.setIsReadNotifi(notifiId).then(
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
                    this.notification[index].isRead = this.response.result;
                    this.counNotRead = this.counNotRead - 1;
                }
            }
        },
        async getNotification() {
            await notificationService.getNotifi().then(
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
                this.notification = this.response.result.listNotifi;
                this.initialNotifi = [...this.notification];
                this.counNotRead = this.response.result.countNotRead;
            }

        },
        logOut() {
            this.$store.dispatch('auth/logout');
            this.$router.push('/login')
        },
        getId() {
            return this.$store.state.auth.user.id;
        },
        pushSearch() {
            window.location.href = "/search?txtSearch=" + this.txtSearch
        },
        ban(isBan) {
            this.centerDialogVisible = isBan;
        },
        async getUserInfo() {
            var currentDate = new Date().getTime();
            var loginDate = new Date(this.$store.state.auth.user.createdDatetime).getTime();
            setTimeout(() => {
                this.centerDialogVisible = true;
            }, this.$store.state.auth.user.jwtExpirationMs - (currentDate - loginDate));
            await userService.getUser(this.$store.state.auth.user.id).then(
                (response) => {
                    if (response.data === "")
                        this.centerDialogVisible = true;
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response

                }
            );
            if (this.response.status == "SUCCESS") {
                this.user = this.response.result;
            }

        },
        formatRelativeTime(timestamp) {
            const now = new Date();
            const targetTime = new Date(timestamp);
            const difference = now - targetTime;

            const millisecondsPerMinute = 60 * 1000;
            const millisecondsPerHour = millisecondsPerMinute * 60;
            const millisecondsPerDay = millisecondsPerHour * 24;
            const millisecondsPerWeek = millisecondsPerDay * 7;
            const millisecondsPerMonth = millisecondsPerDay * 30;
            const millisecondsPerYear = millisecondsPerDay * 365;

            if (difference < millisecondsPerMinute) {
                return 'a few seconds';
            } else if (difference < millisecondsPerHour) {
                const minutes = Math.floor(difference / millisecondsPerMinute);
                return `${minutes} minute${minutes > 1 ? 's' : ''}`;
            } else if (difference < millisecondsPerDay) {
                const hours = Math.floor(difference / millisecondsPerHour);
                return `${hours} hour${hours > 1 ? 's' : ''}`;
            } else if (difference < millisecondsPerWeek) {
                const days = Math.floor(difference / millisecondsPerDay);
                return `${days} day${days > 1 ? 's' : ''}`;
            } else if (difference < millisecondsPerMonth) {
                const weeks = Math.floor(difference / millisecondsPerWeek);
                return `${weeks} week${weeks > 1 ? 's' : ''}`;
            } else if (difference < millisecondsPerYear) {
                const months = Math.floor(difference / millisecondsPerMonth);
                return `${months} month${months > 1 ? 's' : ''}`;
            } else {
                const years = Math.floor(difference / millisecondsPerYear);
                return `${years} year${years > 1 ? 's' : ''}`;
            }
        }
    }
}

</script>

<style scoped>
@import url(https://fonts.googleapis.com/css?family=Poppins);
@import url(https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css);
@import url(https://fonts.googleapis.com/icon?family=Material+Icons);
@import url(https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css);
@import url(./navbar.css);
</style>