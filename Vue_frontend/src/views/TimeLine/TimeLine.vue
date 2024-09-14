<template>
    <div class="container profile">
        <ChatBox ref="myChatBox" :userAvatar="user.avatar" :isClose="isCloseChatBox" :friend="friend" @changeClose="(payload) => {
            isCloseChatBox = payload;
        }" @sendUserId="(payload) => {
            friendId = payload;
        }" />
        <div class="colum">

            <!--Submit Post-->
            <div class="col-md-6 offset-md-3">
                <div class="card">
                    <div class="card-body">
                        <div class="flex-fill ps-2">
                            <input type="text" class="form-control rounded-pill bg-white bg-opacity-15"
                                style="padding-right: 120px;" placeholder="Write a post..." />
                            <button type="button" class="button-comment" data-bs-toggle="modal"
                                data-bs-target="#exampleModal">
                                Write a post...
                            </button>
                            <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
                                aria-hidden="true">
                                <div class="modal-dialog modal-lg" style="width: 550px;">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="exampleModalLabel">Post</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                                                <div class="form-group-post">
                                                    <textarea class="form-control rounded-0 shadow-none"
                                                        style="height: 10px; overflow: hidden; border: none;"
                                                        oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                                                        id="exampleFormControlTextarea1" rows="5"></textarea>
                                                </div>
                                                <div v-if="media.length != 0" class="profile-img-list"
                                                    style="margin: -0.2rem; margin-right: 8px;">
                                                    <button type="submit" class="close AClass" @click="media = []"
                                                        style="position: absolute;z-index: 1; top: 0;right: 0;">
                                                        <span>&times;</span>
                                                    </button>
                                                    <div v-for="(item, index) in media.slice(0, 5)" :key="item"
                                                        :class="'profile-img-list-item ' + numImage(index + 1, media.length)">
                                                        <a href="#" data-lity class="profile-img-list-link"
                                                            :style="'margin-top: 1x; margin-right: 1px;' + ((media.length == 1) ? '' : 'max-height: 150px;')">
                                                            <div class="profile-img-content">
                                                                <img v-if="item.type.includes('image')"
                                                                    :src="createURL(item)" alt="">
                                                                <video v-if="item.type.includes('video')"
                                                                    :src="createURL(item)" controls></video>
                                                            </div>
                                                            <div v-if="index == 4 && media.length > 5"
                                                                class="profile-img-number">+{{ media.length - 5 }}</div>
                                                        </a>
                                                    </div>
                                                </div>
                                            </el-scrollbar>

                                            <hr class="mb-1 opacity-1" />
                                            <div class="row text-center fw-bold">
                                                <div class="col">
                                                    <input ref="fileInput" style="display: none" type="file"
                                                        multiple="multiple" accept="image/*,video/*"
                                                        @change="handleFileSelected" />
                                                    <a href="" @click.prevent="handleFileUpload" id="upload_link"
                                                        class="text-body text-opacity-50 text-decoration-none d-block p-2">
                                                        <i class="fa-solid fa-photo-film me-1 d-block d-sm-inline"></i>
                                                        Photo/Video
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary"
                                                data-bs-dismiss="modal">Close</button>
                                            <button type="button" @click.prevent="sumbitPost()"
                                                class="btn btn-primary">Post</button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

            <!-- list post -->
            <ListPost :posts="posts" :loading="loading" :friend="friend" />
        </div>

        <nav id="sidebarMenu" class="sidebar">
            <a style="cursor: pointer" @click=" sendInfoChat(item)" v-for="(item, index) in listFriend" :key="item"
                class="list-group-item list-group-item-action py-2 ripple">
                <el-badge is-dot class="item" style="margin-right: 10px; margin-left: 10px;"
                    :type="(item.status == 'ONLINE') ? 'success' : 'info'">
                    <el-avatar :size="30" style="margin-right: -6px;"
                        :src="(item.avatar != null) ? 'data:image/*;base64,' + item.avatar : unknowAvatar" /></el-badge>
                <span class="highlight" style="line-break: auto">
                    {{ item.firstName + " " + item.lastName + " " }}</span></a>
        </nav>
    </div>
</template>

<script>
import "https://code.jquery.com/jquery-1.10.2.min.js";
import "https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js";
import userService from "@/services/user.service";
import postService from '@/services/post.service';
import { ElMessage } from 'element-plus';
import ListPost from '@/components/List_Post/ListPost.vue';
import friendService from '@/services/friend.service';
import ChatBox from "@/components/ChatBox/ChatBox.vue";


export default {
    name: 'TimeLine',
    components: {
        ListPost,
        ChatBox
    },
    props: {
        message: {
            type: String,
        },
        statusFriend: {
            type: Object
        },
        friend: {
            type: Object
        }
    },
    data() {
        return {
            response: {
                status: "",
                message: "",
                result: null
            },
            postRequest: {
                postId: null,
                userId: "",
                content: null,
                idDeleted: [],
                sharedId: null
            },
            posts: [],
            media: [],
            listFriend: [],
            loading: true,
            isCloseChatBox: true,
            friend: {},
            user: {},
            friendId: null,
            unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
        };
    },
    mounted() {
        this.getUserInfo();
        this.getListFriend();
        this.getAllPost();
    },
    watch: {
        friendId: function (newValue, oldValue) {
            if (this.friendId != null)
                this.sendInfoChat(this.listFriend[this.foundFriendIndex()]);
        },
        message: function (newValue, oldValue) {
            this.$refs.myChatBox.addMessage(newValue);
            this.$refs.myChatBox.setScrollTo();
        },
        statusFriend: function (newValue, oldValue) {
            if (newValue.userId != this.$store.state.auth.user.id)
                this.listFriend[this.foundFriendIndex(newValue.userId)].status = newValue.status;
        }
    },
    methods: {
        sendInfoChat(item) {
            this.isCloseChatBox = false;
            this.friend = item;
            this.$refs.myChatBox.getHistory(item.userId);
            this.$refs.myChatBox.setScrollTo();
        },
        foundFriendIndex(userId = null) {
            if (userId == null)
                return this.listFriend.findIndex(friend => friend.userId === this.friendId);
            else
                return this.listFriend.findIndex(friend => friend.userId === userId);
        },
        async getUserInfo() {
            await userService.getUser(this.$store.state.auth.user.id).then(
                (response) => {
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
        async getAllPost() {
            await postService.getPosts().then(
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
                this.posts = this.response.result;
                this.loading = false;
            }
            if (this.response.status == "ENTITY_NOT_FOUND")
                this.loading = false;


        },
        async getListFriend() {
            await friendService.getListFriend().then(
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
                this.listFriend = this.response.result;
            }


        },
        handleFileUpload() {
            this.$refs.fileInput.click();
        },

        handleFileSelected(event) {
            for (let i = 0; i < event.target.files.length; i++) {
                this.media.push(event.target.files[i]);
            }
            event.target.value = '';
        },
        createURL(itemFile) {
            return URL.createObjectURL(itemFile);
        },
        async sumbitPost(sharedPostId = null) {

            this.postRequest.content = document.getElementById("exampleFormControlTextarea1").value;
            if (this.postRequest.content != null && this.postRequest.content != "" || this.media.length != 0 || sharedPostId != null) {
                this.postRequest.userId = this.$store.state.auth.user.id;
                this.postRequest.sharedId = sharedPostId;
                const formData = new FormData();
                for (let i = 0; i < this.media.length; i++) {
                    formData.append('media', this.media[i]);
                }
                formData.append('post', JSON.stringify(this.postRequest));
                await postService.addPost(formData).then(
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
                    this.postRequest.content = null;
                    document.getElementById("exampleFormControlTextarea1").value = null;
                    this.media = [];
                    ElMessage({
                        message: this.response.message,
                        type: 'success',
                    });
                    this.posts.push(this.response.result);
                    this.loading = false;
                    if (sharedPostId == null)
                        $('#exampleModal').modal('toggle');
                    else
                        $('#exampleModalShare').modal('toggle');
                }
                else {
                    ElMessage.error(this.response.message);
                };
            }
            else {
                ElMessage({
                    message: 'The post cannot be empty.',
                    type: 'warning',
                })
            }
        },
        numImage(num, length) {
            switch (length) {
                case 1:
                    return "one";
                case 2:
                    return "one";
                case 3:
                    return (num == 1) ? "one" : "two";
                case 4:
                    return (num == 1) ? "one" : "three";
                case 5:
                    return (num == 1 || num == 2) ? "two" : "three";
                default:
                    return (num == 1 || num == 2) ? "two" : (num != 5) ? "three" : "with-number";
            }

        }
    }
}
</script>

<style scoped>
@import url(./TimeLine.css);
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css);
@import url(https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css);
</style>