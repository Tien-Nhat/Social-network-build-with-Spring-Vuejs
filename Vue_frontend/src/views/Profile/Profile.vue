<template>
  <el-dialog v-model="dialogFormVisible" title="Shipping address" width="500" :before-close="changeAbout">
    <el-form :model="form">
      <el-form-item label="About" label-width=140px>
        <el-input v-model.lazy="this.user.introduce" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Address" label-width=140px>
        <el-select v-model.lazy="this.user.address" placeholder="Please select a zone">
          <el-option label="Huế" value="Thừa Thiên Huế" />
          <el-option label="Đà Nẵng" value="Đà Nẵng" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="dialogFormVisible = false">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>
  <section class="h-100 gradient-custom-2">
    <div class="container">
      <div class="profile-page tx-13">
        <div class="row">
          <div class="col-12 grid-margin">
            <div class="profile-header" style="margin-bottom: 0px;">
              <div class="cover">
                <!-- <div class="gray-shade"></div> -->
                <div class="d-flex justify-content-between align-items-center" style="position: absolute;z-index: 7;right: 0; bottom: 0;
                  z-index: 3;
                  width: 100%;
                  padding: 0 20px;">
                  <div></div>
                  <div class="d-flex flex-row">
                    <div v-if="!isHomeUser" class="p-2">
                      <button v-if="user.friendStatus == 'NOT_FRIEND'"
                        class="btn btn-primary btn-icon-text btn-edit-profile" @click="addFriend">
                        <i class="fa fa-user-plus"></i> Add friend
                      </button>
                      <button v-if="user.friendStatus == 'PENDING'"
                        class="btn btn-primary btn-icon-text btn-edit-profile" @click="addFriend">
                        <i class="fas fa-hourglass"></i> Cancel invite.
                      </button>

                      <el-dropdown v-if="user.friendStatus == 'ACCEPTED'" trigger="click">
                        <button class="btn btn-primary btn-icon-text btn-edit-profile">
                          <i :class="changeFriendIcon()"></i> {{ formatRequestFriend() }}
                        </button>
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item @click="declineFriend()"><i class="fa-solid fa-x" aria-hidden="true"
                                style="padding-right: 2px;" />
                              Unfriend
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>

                    </div>
                    <div v-if="isHomeUser" class="p-2">
                      <input ref="fileInputBackground" style="display: none" type="file" accept="image/*"
                        @change="handleFileSelectedBackground" />
                      <button class="btn btn-primary btn-icon-text btn-edit-profile"
                        @click.prevent="handleFileUpload('background')">
                        <i class="fa-regular fa-pen-to-square"></i> Change background
                      </button>
                    </div>
                  </div>
                </div>
                <figure>
                  <el-image style="width: 1294px; height: 500px"
                    :src="(user.background != null) ? 'data:image/*;base64,' + user.background : defaultBackgroud"
                    fit="cover" />
                </figure>
                <div class="cover-body d-flex justify-content-between align-items-center">
                  <div>
                    <img class="profile-pic"
                      :src="(user.avatar != null) ? 'data:image/*;base64,' + user.avatar : unknowAvatar" alt="profile">
                    <input ref="fileInputAvatar" style="display: none" type="file" accept="image/*"
                      @change="handleFileSelected" />
                    <button v-if="isHomeUser" @click.prevent="handleFileUpload('avatar')" class="half-circle-btn">Change
                      Avatar</button>
                    <span class="profile-name">{{ user.firstName }} {{ user.lastName }}</span>
                  </div>
                </div>
              </div>
              <div class="header-links">

              </div>
            </div>
          </div>
        </div>
        <div class="row profile-body" style="background-color: aliceblue; margin-top: 0px;">

          <div class="d-none d-md-block col-md-4 col-xl-3 left-wrapper">
            <div class="card rounded">
              <div class="card-body">
                <div class="d-flex align-items-center justify-content-between mb-2">
                  <h6 class="card-title mb-0">About</h6>
                  <div class="dropdown">
                    <button class="btn p-0" type="button" id="dropdownMenuButton" data-toggle="dropdown"
                      aria-haspopup="true" aria-expanded="false">
                      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                        stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                        class="feather feather-more-horizontal icon-lg text-muted pb-3px">
                        <circle cx="12" cy="12" r="1"></circle>
                        <circle cx="19" cy="12" r="1"></circle>
                        <circle cx="5" cy="12" r="1"></circle>
                      </svg>
                    </button>
                    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                      <a class="dropdown-item d-flex align-items-center" plain @click="dialogFormVisible = true">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                          class="feather feather-edit-2 icon-sm mr-2">
                          <path d="M17 3a2.828 2.828 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5L17 3z">
                          </path>
                        </svg> <span class>Edit</span></a>
                      <a class="dropdown-item d-flex align-items-center" href="#">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                          class="feather feather-git-branch icon-sm mr-2">
                          <line x1="6" y1="3" x2="6" y2="15"></line>
                          <circle cx="18" cy="6" r="3"></circle>
                          <circle cx="6" cy="18" r="3"></circle>
                          <path d="M18 9a9 9 0 0 1-9 9"></path>
                        </svg> <span class>Update</span></a>
                      <a class="dropdown-item d-flex align-items-center" href="#">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                          stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                          class="feather feather-eye icon-sm mr-2">
                          <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                          <circle cx="12" cy="12" r="3"></circle>
                        </svg> <span class>View all</span></a>
                    </div>
                  </div>
                </div>

                <div class="d-flex align-items-center mb-2">

                  <textarea v-if="isEditIntroduce" class="form-control rounded-0 "
                    style="height: 10px; overflow: hidden;"
                    oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                    id="exampleFormControlTextarea1" v-model="user.introduce" rows="5"></textarea>
                  <span v-else class="text-muted">
                    {{ (user.introduce) ? user.introduce : 'Write introduce here...' }}</span>

                  <div class="flex-fill ps-2"></div>

                  <button v-if="isHomeUser && !isEditIntroduce" @click="isEditIntroduce = true" class="btn p-0"
                    type="button">
                    <i class="fa-solid fa-pen"></i>
                  </button>
                  <button v-if="isEditIntroduce" @click="editUser" class="btn p-0" type="button">
                    <i class="fa-solid fa-check"></i>
                  </button>
                </div>
                <div class="mt-3">
                  <label class="tx-11 font-weight-bold mb-0 text-uppercase">Joined:</label>
                  <p class="text-muted">{{ formatDate(user.joined) }}</p>
                </div>
                <div class="mt-3">
                  <label class="tx-11 font-weight-bold mb-0 text-uppercase">Lives:</label>
                  <p class="text-muted">{{ user.address }}</p>
                </div>

              </div>
            </div>
          </div>

          <div class="modal fade" id="exampleModalEdit" tabindex="-1" aria-labelledby="exampleModalLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-lg" style="width: 644px;">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">Post</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                  <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                    <div class="form-group-post">
                      <textarea class="form-control rounded-0 shadow-none" style="height: 10px; overflow: hidden;"
                        oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                        id="exampleFormControlTextarea1" rows="5" v-model="postEdit.content"></textarea>
                    </div>
                    <div v-if="postEdit.media.length != 0 || media != null" class="profile-img-list">
                      <div class="profile-img-list-item one">
                        <a data-lity class="profile-img-list-link" style="margin-top: 10px;">
                          <div class="block text-center" m="t-4">
                            <el-carousel height="auto" trigger="click" :autoplay="false">
                              <el-carousel-item style="height: auto" v-for="item in postEdit.media" :key="item">
                                <div class="profile-img-content">
                                  <button type="submit" class="close AClass" @click="unSelectMedia(item);"
                                    style="position: absolute;z-index: 1; top: 0;right: 0;">
                                    <span>&times;</span>
                                  </button>
                                  <img
                                    v-if="(isFile(item)) ? item.type.includes('image') : item.contentType.startsWith('image')"
                                    :src="(!isFile(item)) ? 'data:' + item.contentType + ';base64,' + item.base64String : createURL(item)"
                                    alt="">
                                  <video v-else style="outline: none; border: none; box-shadow: none;"
                                    :src="(!isFile(item)) ? 'data:' + item.contentType + ';base64,' + item.base64String : createURL(item)"
                                    controls>
                                  </video>
                                </div>
                              </el-carousel-item>
                            </el-carousel>
                          </div>
                        </a>
                      </div>
                    </div>
                  </el-scrollbar>

                  <hr class="mb-1 opacity-1" />
                  <div class="row text-center fw-bold">
                    <div class="col">
                      <input ref="fileInput" style="display: none" type="file" accept="image/*,video/*"
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
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                  <button type="button" @click.prevent="sumbitEditPost(postEdit.postId)"
                    class="btn btn-primary">Save</button>
                </div>
              </div>
            </div>
          </div>


          <!-- popup submit post -->
          <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-lg" style="width: 500px;">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">Post</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                  <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                    <div class="form-group-post">
                      <textarea class="form-control rounded-0 shadow-none" style="height: 10px; overflow: hidden;"
                        oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                        id="exampleFormControlTextarea1" rows="5" v-model="postRequest.content"></textarea>
                    </div>

                    <div v-if="media.length != 0" class="profile-img-list" style="margin: -0.2rem; margin-right: 8px;">
                      <button type="submit" class="close AClass" @click="media = []"
                        style="position: absolute;z-index: 1; top: 0;right: 0;">
                        <span>&times;</span>
                      </button>
                      <div v-for="(item, index) in media.slice(0, 5)" :key="item"
                        :class="'profile-img-list-item ' + numImage(index + 1, media.length)">
                        <a href="#" data-lity class="profile-img-list-link"
                          :style="'margin-top: 1x; margin-right: 1px;' + ((media.length == 1 && item.type.includes('video')) ? '' : 'max-height: 150px;')">
                          <div class="profile-img-content">
                            <img v-if="item.type.includes('image')" :src="createURL(item)" alt="">
                            <video v-if="item.type.includes('video')" :src="createURL(item)"></video>
                          </div>
                          <div v-if="index == 4 && media.length > 5" class="profile-img-number">+{{ media.length - 5
                            }}</div>
                        </a>
                      </div>
                    </div>
                  </el-scrollbar>
                  <hr class="mb-1 opacity-1" />
                  <div class="row text-center fw-bold">
                    <div class="col">
                      <input ref="fileInputPost" style="display: none" type="file" multiple="multiple"
                        accept="image/*,video/*" @change="handleFileSelectedPost" />
                      <a href="" @click.prevent="handleFileUpload('post')" id="upload_link"
                        class="text-body text-opacity-50 text-decoration-none d-block p-2">
                        <i class="fa-solid fa-photo-film me-1 d-block d-sm-inline"></i>
                        Photo/Video
                      </a>
                    </div>
                  </div>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                  <button type="button" @click.prevent="sumbitPost" class="btn btn-primary">Submit</button>
                </div>
              </div>
            </div>
          </div>


          <!-- popup list liked -->
          <div class="modal fade" id="exampleModalLiked" tabindex="-1" aria-labelledby="exampleModalLabel"
            aria-hidden="true">
            <div class="modal-dialog modal-lg" style="width: 550px;">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">List user liked</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                  <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                    <div v-for="user in this.listLiked" :key="user" class="d-flex align-items-center mb-3">
                      <a :href="'/profile/' + user.userId"><img
                          :src="(user.avatar != null) ? 'data:image/*;base64,' + user.avatar : unknowAvatar" alt=""
                          width="50" height="50" class="rounded-circle" /></a>
                      <div class="flex-fill ps-2">
                        <div class="fw-bold">
                          <a :href="'/profile/' + user.userId" class="text-decoration-none">
                            {{ user.firstName }} {{ user.lastName }}
                          </a>
                        </div>
                      </div>
                      <i class="fa-solid fa-thumbs-up me-1 d-block d-sm-inline"></i>
                    </div>

                  </el-scrollbar>
                </div>
              </div>
            </div>
          </div>

          <div class="col-md-8 col-xl-6 middle-wrapper">
            <div class="row">
              <div class="col-md-12 grid-margin">
                <div class="card rounded">
                  <div class="card-footer">
                    <div class="d-flex align-items-center">
                      <div class="flex-fill ps-2">
                        <div class="position-relative d-flex align-items-center">
                          <input type="text" class="form-control rounded-pill bg-white bg-opacity-15"
                            style="padding-right: 120px;" placeholder="Write a post..." />
                          <button type="button" class="button-comment" data-bs-toggle="modal"
                            data-bs-target="#exampleModal">
                            Write a post...
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="container profile">
                <div class="colum">
                  <ListPost :posts="sortedArray" :loading="loading" :friend="friend" />
                </div>
              </div>
            </div>
          </div>

          <!-- popup full media Post -->
          <div class="modal fade p-0" id="exampleModalUserImage" tabindex="-1" aria-labelledby="exampleModalUserImage"
            aria-hidden="true">
            <div class="modal-dialog modal-fullscreen p-0">
              <div class="modal-content">
                <div class="modal-body" style="padding: 0%;">
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  <DetailMedia :listMedia="listPhoto" :indexMedia="indexMedia" :user="userMedia"
                    ref="detailUserMedia" />
                </div>
              </div>
            </div>
          </div>

          <div class="d-none d-xl-block col-xl-3 right-wrapper">
            <div class="row">
              <div class="col-md-12 grid-margin">
                <div class="card rounded">
                  <div class="card-body">
                    <h6 class="card-title">latest photos</h6>
                    <div class="latest-photos">
                      <div class="row">
                        <div v-for="(item, index) in listPhoto.slice(0, 9)" class="col-md-4">
                          <figure>
                            <el-image @click="zoomImage(index)" style="width: 80px; height: 80px"
                              :src="'data:image/*;base64,' + item.base64String" fit="cover" />
                          </figure>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
<script>
import Navbar from '@/components/Navbar/Navbar.vue';
import "https://code.jquery.com/jquery-1.10.2.min.js";
import postService from '@/services/post.service';
import userService from '@/services/user.service';
import { ElMessage, ElMessageBox } from 'element-plus'
import friendService from '@/services/friend.service';
import ListPost from '@/components/List_Post/ListPost.vue';
import DetailMedia from "@/components/Detail_Media/DetailMedia.vue";

export default {
  name: 'TimeLine',
  components: {
    Navbar,
    ListPost,
    DetailMedia
  },
  props: {
    friend: {
      type: Object
    }
  },
  watch: {
    friend: function (newValue, oldValue) {
      this.user.friendStatus = newValue.status;
    },
  },
  data() {
    return {
      centerDialogVisible: false,
      response: {
        status: "",
        message: "",
        result: null
      },
      successful: false,
      postRequest: {
        postId: null,
        userId: "",
        content: null,
        idDeleted: []
      },
      user: {},
      delete: new Set(),
      media: null,
      postEdit: {
        content: null,
        media: [],
        postId: null
      },
      media: [],
      mediaDeleted: [],
      fileSelectedToAdd: [],
      urlMedia: null,
      loading: true,
      posts: [],
      isEditIntroduce: false,
      dialogFormVisible: false,
      listLiked: [],
      listPhoto: [],
      indexMedia: 0,
      userMedia: {
        firstName: null,
        lastName: null,
        createdDatetime: null
      },
      unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais",
      defaultBackgroud: "https://i.pinimg.com/originals/c5/40/01/c540018ca1c7b93cb1fbc218ea0c73a7.png"
    }
  },
  computed: {
    sortedArray: function () {
      function compare(a, b) {
        if (a.createdDatetime > b.createdDatetime)
          return -1;
        if (a.createdDatetime < b.createdDatetime)
          return 1;
        return 0;
      }
      if (this.posts != null)
        return this.posts.sort(compare);
      else return this.posts;
    },
    isHomeUser: function () {
      return (this.$route.params.id == this.$store.state.auth.user.id)
    }
  },

  mounted() {
    this.getUserPost();
    this.getUserInfo();
    this.getPhoto();
  },

  methods: {
    async getPhoto() {
      await userService.getPhoto().then(
        (response) => {
          this.response = response.data;
        },
        (error) => {
          this.response =
            error.response
        }
      );
      if (this.response.status == "SUCCESS") {
        this.listPhoto = this.response.result;
        this.userMedia.createdDatetime = this.listPhoto[0].postDate;
      }

    },
    zoomImage(index) {
      this.userMedia.firstName = this.user.firstName;
      this.userMedia.lastName = this.user.lastName;
      this.indexMedia = index;


      this.$refs.detailUserMedia.setActiveItem();
      $('#exampleModalUserImage').modal('toggle');
    },
    async declineFriend() {
      await friendService.deleteFriend(this.$route.params.id).then(
        (response) => {
          this.response = response.data;
        },
        (error) => {
          this.response =
            error.response
        }
      );
      if (this.response.status == "SUCCESS") {
        this.user.friendStatus = this.response.result;

      }

    },
    formatRequestFriend() {
      switch (this.user.friendStatus) {
        case "PENDING":
          return "Cancel invite.";
        case "ACCEPTED":
          return "Friend.";
        case "NOT_FRIEND":
          return "Add friend.";
        default:
          return "Add friend.";
      }
    },
    changeFriendIcon() {
      switch (this.user.friendStatus) {
        case "PENDING":
          return "fas fa-hourglass";
        case "ACCEPTED":
          return "fa-solid fa-user-check";
        case "NOT_FRIEND":
          return "fa fa-user-plus";
        default:
          return "fa fa-user-plus";
      }
    },
    async addFriend() {
      await friendService.addFriend(this.$route.params.id).then(
        (response) => {
          this.response = response.data;
        },
        (error) => {
          this.response =
            error.response
        }
      );
      if (this.response.status == "SUCCESS") {
        this.user.friendStatus = this.response.result;
      }
      else {
        ElMessage.error(this.response.message);
      };
    },
    callListLiked(listLiked) {
      this.listLiked = listLiked

      $('#exampleModalLiked').modal('toggle');
    },
    async editUser() {
      ElMessageBox.confirm(
        'Are you sure you want to change the info user?',
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'warning',
        })
        .then(async () => {
          this.user.userId = this.$store.state.auth.user.id;
          await userService.editUser(this.user).then(
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
              if (error.response.data.status == "UNAUTHORIZED")
                this.centerDialogVisible = true;
            }
          );
          if (this.response.status == "SUCCESS") {
            this.isEditIntroduce = false;
            ElMessage({
              message: this.response.message,
              type: 'success',
            });
          }
          else {
            ElMessage.error(this.response.message);
          };
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: 'Change canceled',
          })
        }
        )

    },
    logOut() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/login');
    },
    async getUserPost() {
      await postService.getPosts(this.$route.params.id).then(
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

          if (error.response.data.status == "UNAUTHORIZED")
            this.centerDialogVisible = true;
        }
      );
      this.posts = this.response.result;
      this.loading = false;
    },

    async getUserInfo() {
      await userService.getUser(this.$route.params.id).then(
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

          if (error.response.data.status == "UNAUTHORIZED")
            this.centerDialogVisible = true;
        }
      );

      this.user = this.response.result;
    },
    async likePost(postId, index) {

      await postService.likePost(postId).then(
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
        this.posts[index].liked = this.response.result;
        var userInfo = {
          avatar: this.user.avatar,
          userId: this.$store.state.auth.user.id,
          firstName: this.user.firstName,
          lastName: this.user.lastName
        };
        if (this.response.result)
          this.posts[index].listLiked.unshift(userInfo);
        else
          this.posts[index].listLiked.shift();
      }
    },
    textCountLike(listLiked) {
      var nameFirstPerson = "";
      if (listLiked.length > 0 && listLiked[0].userId == this.$store.state.auth.user.id)
        nameFirstPerson = "You"

      else if (listLiked.length > 0)
        nameFirstPerson = listLiked[0].firstName + " " + listLiked[0].lastName

      switch (listLiked.length) {
        case 0:
          return "";
        case 1:
          return listLiked[0].firstName + " " + listLiked[0].lastName + " liked"
        case 2:
          return nameFirstPerson + " and " + listLiked[1].firstName + " " + listLiked[1].lastName + " liked"
        case 3:
          return nameFirstPerson + ", " + listLiked[1].firstName + " " + listLiked[1].lastName + " and " + listLiked[2].firstName + " " + listLiked[2].lastName + " liked"
        default:
          return nameFirstPerson + ", " + listLiked[1].firstName + " " + listLiked[1].lastName + ", " + listLiked[2].firstName + " " + listLiked[2].lastName + " and " + (listLiked.length - 3) + " others liked";
      }
    },
    async sumbitPost() {
      if (this.postRequest.content != null && this.postRequest.content != "" || this.media.length != 0) {
        this.postRequest.userId = this.$store.state.auth.user.id;
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
          this.getUserPost();
          this.postRequest.content = null;
          this.media = [];
          ElMessage({
            message: this.response.message,
            type: 'success',
          });
          $('#exampleModal').modal('toggle');
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

    changeAbout() {
      this.user.address = null;
      this.user.introduce = null;
      this.dialogFormVisible = false;
    },

    formatDate(inputDate) {
      const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
      const date = new Date(inputDate);
      const month = months[date.getMonth()];
      const day = date.getDate();
      const year = date.getFullYear();
      return `${month} ${day}, ${year}`;
    },

    handleFileUpload(typeImage) {
      switch (typeImage) {
        case "avatar":
          this.$refs.fileInputAvatar.click();
          break;
        case "background":
          this.$refs.fileInputBackground.click();
          break;
        default:
          this.$refs.fileInputPost.click();
          break;
      }


    },

    handleFileSelected(event) {
      this.changeAvatar(event.target.files[0]);
      event.target.value = '';
    },
    handleFileSelectedBackground(event) {
      this.changeBackground(event.target.files[0]);
      event.target.value = '';
    },
    handleFileSelectedPost(event) {
      for (let i = 0; i < event.target.files.length; i++) {
        this.media.push(event.target.files[i]);
      }
      event.target.value = '';
    },

    async changeBackground(background) {
      ElMessageBox.confirm(
        'Are you sure you want to change the background?',
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'warning',
        })
        .then(async () => {
          const formData = new FormData();
          formData.append('background', background);
          formData.append('id', this.$store.state.auth.user.id);
          await userService.changeBackground(formData).then(
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
              if (error.response.data.status == "UNAUTHORIZED")
                this.centerDialogVisible = true;
            }
          );
          this.user.background = this.response.result;
          ElMessage({
            type: 'success',
            message: 'Change completed',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: 'Change canceled',
          })
        }
        )
    },

    async changeAvatar(avatar) {
      ElMessageBox.confirm(
        'Are you sure you want to change the avatar?',
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'warning',
        })
        .then(async () => {
          const formData = new FormData();
          formData.append('avatar', avatar);
          formData.append('id', this.$store.state.auth.user.id);
          await userService.changeAvatar(formData).then(
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
              if (error.response.data.status == "UNAUTHORIZED")
                this.centerDialogVisible = true;
            }
          );
          this.user.avatar = this.response.result;
          ElMessage({
            type: 'success',
            message: 'Change completed',
          })
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: 'Change canceled',
          })
        }
        )

    },
    createURL(itemFile) {
      return URL.createObjectURL(itemFile);
    },
    editPost(post) {
      this.postEdit.media = post.medias;
      this.postEdit.content = post.content;
      this.postEdit.postId = post.id;
      $('#exampleModalEdit').modal('toggle');
    },

    isImage(url) {
      return /\.(jpg|jpeg|png|webp|avif|gif|svg|jfif)$/.test(url);
    },
    isFile(item) {
      return item instanceof File;
    },
    isDelete(postId) {
      return this.delete.has(postId);
    },

    async sumbitEditPost(postId) {
      if (this.postEdit.content != null && this.postEdit.content != "" || this.fileSelectedToAdd.length != 0 || this.postEdit.media.length != 0) {

        this.postRequest.userId = this.$store.state.auth.user.id;
        this.postRequest.content = this.postEdit.content;
        this.postRequest.postId = postId;
        this.postRequest.idDeleted = this.mediaDeleted;

        const formData = new FormData();
        for (let i = 0; i < this.fileSelectedToAdd.length; i++) {
          formData.append('media', this.fileSelectedToAdd[i]);
        }
        formData.append('post', JSON.stringify(this.postRequest));
        await postService.editPost(formData).then(
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
          this.getUserPost();
          this.postRequest.content = null;
          ElMessage({
            message: this.response.message,
            type: 'success',
          });
          $('#exampleModalEdit').modal('toggle');
          this.fileSelectedToAdd = [];
          this.postEdit.content = null;
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
    unSelectMedia(itemToRemove) {
      if (!this.isFile(itemToRemove))
        this.mediaDeleted.push(itemToRemove.id);
      else
        this.fileSelectedToAdd = this.fileSelectedToAdd.filter(item => item !== itemToRemove);
      this.postEdit.media = this.postEdit.media.filter(item => item !== itemToRemove);
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

    },
    async deletePost(postId) {
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
          if (error.response.data.status == "UNAUTHORIZED")
            this.centerDialogVisible = true;
        }
      );
      if (this.response.status == "SUCCESS") {
        this.delete.add(postId);
        ElMessage({
          message: this.response.message,
          type: 'success',
        });
      }
      else if (!this.centerDialogVisible) {
        ElMessage.error(this.response.message);
      };
    },

    async restorePost(postId) {
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
          if (error.response.data.status == "UNAUTHORIZED")
            this.centerDialogVisible = true;
        }
      );

      if (this.response.status == "SUCCESS") {
        this.delete.delete(postId);
        ElMessage({
          message: this.response.message,
          type: 'success',
        });
      }
      else if (!this.centerDialogVisible) {
        ElMessage.error(this.response.message);
      };
    },



  }

}
</script>
<style scoped>
@import url(./Profile.css);
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css);
</style>