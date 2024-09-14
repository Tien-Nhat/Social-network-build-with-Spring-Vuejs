<template>
    <!-- Popup Edit Pots -->
    <div class="modal fade" id="exampleModalEdit" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" style="width: 644px;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Post</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                        <div class="form-group-post">
                            <textarea class="form-control rounded-0 shadow-none"
                                style="height: 10px; overflow: hidden; border: none;"
                                oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                                id="exampleFormControlTextarea1" rows="5" v-model="postEdit.content"></textarea>
                        </div>

                        <div v-if="isShare" class="card">
                            <button type="submit" class="close AClass" @click="isShare = false"
                                style="position: absolute;z-index: 1; top: 0;right: 0;">
                                <span>&times;</span>
                            </button>
                            <div class="card-body">
                                <div class="d-flex align-items-center mb-3">
                                    <a :href="'/profile/' + postShare.userId"><img
                                            :src="(postShare.avatar != null) ? 'data:image/*;base64,' + postShare.avatar : unknowAvatar"
                                            alt="" width="50" height="50" class="rounded-circle" /></a>
                                    <div class="flex-fill ps-2">
                                        <div class="fw-bold">
                                            <a :href="'/profile/' + postShare.userId" class="text-decoration-none">
                                                {{ postShare.firstName }} {{ postShare.lastName }}
                                            </a>
                                        </div>
                                        <div class="small text-body text-opacity-50">
                                            {{ postShare.createdDatetime }}
                                        </div>
                                    </div>
                                </div>
                                <!-- content shared Post -->
                                <p style="white-space: pre-wrap;">{{ postShare.content }}</p>
                                <div v-if="postShare.medias.length != 0" class="profile-img-list"
                                    style="margin: -0.2rem; margin-right: 8px;">
                                    <div v-for="(item2, index) in postShare.medias.slice(0, 5)" :key="item2"
                                        :class="'profile-img-list-item ' + numImage(index + 1, postShare.medias.length)">
                                        <a href=" #" data-lity class="profile-img-list-link"
                                            :style="'margin-top: 1x; margin-right: 1px;' + ((postShare.medias.length == 1 && item2.contentType.startsWith('video')) ? '' : ((postShare.medias.length == 1 && item2.contentType.startsWith('image')) ? 'max-height: 850px;' : ''))">
                                            <div class="profile-img-content">
                                                <img v-if="item2.contentType.startsWith('image')"
                                                    :src="'data:' + item2.contentType + ';base64,' + item2.base64String"
                                                    alt=""
                                                    :style="(numImage(index + 1, postShare.medias.length) == 'one' && postShare.medias.length > 1) ? 'max-height:400px' : ''">
                                                <video v-if="item2.contentType.startsWith('video')"
                                                    :src="'data:' + item2.contentType + ';base64,' + item2.base64String"
                                                    controls></video>
                                            </div>
                                            <div v-if="index == 4 && postShare.medias.length > 5"
                                                class="profile-img-number">
                                                +{{ postShare.medias.length - 5 }}</div>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div v-if="postEdit.media.length != 0 && !isShare" class="profile-img-list">
                            <div class="profile-img-list-item one">
                                <a data-lity class="profile-img-list-link" style="margin-top: 10px;">
                                    <div class="block text-center" m="t-4">
                                        <el-carousel height="auto" trigger="click" :autoplay="false">
                                            <el-carousel-item style="height: auto" v-for="item in postEdit.media"
                                                :key="item">
                                                <div class="profile-img-content">
                                                    <button type="submit" class="close AClass"
                                                        @click="unSelectMedia(item);"
                                                        style="position: absolute;z-index: 1; top: 0;right: 0;">
                                                        <span>&times;</span>
                                                    </button>
                                                    <img v-if="(isFile(item)) ? item.type.includes('image') : item.contentType.startsWith('image')"
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
                    <div v-if="!isShare" class="row text-center fw-bold">
                        <div class="col">
                            <input ref="fileInput123" style="display: none" type="file" multiple="multiple"
                                accept="image/*,video/*" @change="handleFileSelectedEdit" />
                            <a href="" @click.prevent="handleFileUploadEdit" id="upload_link"
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

    <!-- Popup share Post -->
    <div class="modal fade" id="exampleModalShare" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" style="width: 500px;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Share Post</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                        <div class="form-group-post">
                            <textarea class="form-control rounded-0 shadow-none"
                                style="height: 10px; overflow: hidden; border: none;"
                                oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                                id="exampleFormControlTextarea1" rows="5" v-model="postRequest.content"></textarea>
                        </div>
                        <div class="card">
                            <div class="card-body">
                                <div class="d-flex align-items-center mb-3">
                                    <a :href="'/profile/' + postShare.userId"><img
                                            :src="(postShare.avatar != null) ? 'data:image/*;base64,' + postShare.avatar : unknowAvatar"
                                            alt="" width="50" height="50" class="rounded-circle" /></a>
                                    <div class="flex-fill ps-2">
                                        <div class="fw-bold">
                                            <a :href="'/profile/' + postShare.userId" class="text-decoration-none">
                                                {{ postShare.firstName }} {{ postShare.lastName }}
                                            </a>
                                        </div>
                                        <div class="small text-body text-opacity-50">
                                            {{ postShare.createdDatetime }}
                                        </div>
                                    </div>
                                </div>
                                <!-- content Post -->
                                <p style="white-space: pre-wrap;">{{ postShare.content }}</p>
                                <div v-if="postShare.medias.length != 0" class="profile-img-list"
                                    style="margin: -0.2rem; margin-right: 8px;">
                                    <div v-for="(item2, index) in postShare.medias.slice(0, 5)" :key="item2"
                                        :class="'profile-img-list-item ' + numImage(index + 1, postShare.medias.length)">
                                        <a href=" #" data-lity class="profile-img-list-link"
                                            :style="'margin-top: 1x; margin-right: 1px;' + ((postShare.medias.length == 1 && item2.contentType.startsWith('video')) ? '' : ((postShare.medias.length == 1 && item2.contentType.startsWith('image')) ? 'max-height: 850px;' : ''))">
                                            <div class="profile-img-content">
                                                <img v-if="item2.contentType.startsWith('image')"
                                                    :src="'data:' + item2.contentType + ';base64,' + item2.base64String"
                                                    alt=""
                                                    :style="(numImage(index + 1, postShare.medias.length) == 'one' && postShare.medias.length > 1) ? 'max-height:400px' : ''">
                                                <video v-if="item2.contentType.startsWith('video')"
                                                    :src="'data:' + item2.contentType + ';base64,' + item2.base64String"
                                                    controls></video>
                                            </div>
                                            <div v-if="index == 4 && postShare.medias.length > 5"
                                                class="profile-img-number">
                                                +{{ postShare.medias.length - 5 }}</div>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </el-scrollbar>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" @click.prevent="sumbitPost(postShare.postId)"
                        class="btn btn-primary">Share</button>
                </div>
            </div>
        </div>
    </div>

    <!-- popup list liked -->
    <div class="modal fade" id="exampleModalLiked" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
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
                                    :src="(user.avatar != null) ? 'data:image/*;base64,' + user.avatar : unknowAvatar"
                                    alt="" width="50" height="50" class="rounded-circle" /></a>
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

    <!-- popup full media Post -->
    <div class="modal fade" id="exampleModalPost" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-fullscreen p-0">
            <div class="modal-content">
                <div class="modal-body" style="padding: 0%;">
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    <DetailMedia :listMedia="listMedia" :indexMedia="indexMedia" :user="userDetailMedia"
                        ref="detailMedia" />
                </div>
            </div>
        </div>
    </div>

    <!-- popup report Post -->
    <div class="modal fade" id="exampleModalReport" tabindex="-1" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-lg" style="width: 500px;">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Report Post</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                        <p style="margin-top: 10px;">Report type</p>
                        <select class="form-select" v-model="selectReportType" aria-label="Default select example">
                            <option v-for="item in listReportType" :key="item" :value="item.id">{{ item.name }}</option>
                        </select>
                        <p style="margin-top: 10px;">Reason</p>
                        <div class="form-group-post">
                            <textarea class="form-control rounded-0 shadow-none"
                                style="height: 100px; overflow: hidden;"
                                oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                                id="exampleFormControlTextarea1"
                                placeholder="Please write the reason for the report here." rows="5"
                                v-model="report.reason"></textarea>
                        </div>
                    </el-scrollbar>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" @click.prevent="submitReport()" class="btn btn-primary">Report</button>
                </div>
            </div>
        </div>
    </div>

    <!-- list Post -->
    <el-skeleton :class="classPost()" style="width: 970px" :loading="loading" animated :count="3">
        <!-- screen wait load Post -->
        <template #template>
            <div style="padding: 14px;">
                <el-skeleton-item variant="h3" style="width: 50%" />
                <div
                    style="display: flex; align-items: center; justify-items: space-between; margin-top: 16px; height: 16px;">
                    <el-skeleton-item variant="text" style="margin-right: 16px" />
                    <el-skeleton-item variant="text" style="width: 30%" />
                </div>
            </div>
            <el-skeleton-item variant="image" style="width: 610px; height: 267px" />
        </template>
        <!-- loaded Post -->
        <template #default>
            <el-empty v-if="posts.length == 0 || posts == null" description="There are no posts here." />
            <div v-else v-for="(item, index) in sortedArray" :key="item" :class="classPost()">

                <div class="card">
                    <!-- cardBody Notification deleted -->
                    <div v-if="isDelete(item.id)" class="card-body">
                        <div class="flex-fill ps-2">
                            <el-result icon="info">
                                <template #sub-title>
                                    <p>Your post has been deleted. You can undo the deletion before we
                                        permanently
                                        remove it.</p>
                                </template>
                                <template #extra>
                                    <el-button type="primary" @click="restorePost(item.id)">Undo</el-button>
                                </template>
                            </el-result>
                        </div>
                    </div>
                    <!-- cardBody if have post -->
                    <div v-else class="card-body">
                        <div class="d-flex align-items-center mb-3">
                            <PopOver :user="item" :mapFriendStatus="mapFriendStatus" />
                            <div class="flex-fill ps-2">
                                <div class="fw-bold">
                                    <a :href="'/profile/' + item.userId" class="text-decoration-none">
                                        {{ item.firstName }} {{ item.lastName }}
                                    </a>
                                </div>
                                <a :href="'/post/' + item.id" class="small text-body text-opacity-50">{{
                                    formatDate(item.createdDatetime) }}
                                </a>
                            </div>
                            <!-- function menu with post -->
                            <el-dropdown trigger="click">
                                <el-icon style="outline: none;">
                                    <MoreFilled />
                                </el-icon>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item v-if="isHomeUser(item.userId)" @click="editPost(item)">
                                            <Edit style="width: 1em; height: 1em; margin-right: 8px" />Edit
                                            post
                                        </el-dropdown-item>
                                        <el-dropdown-item v-if="isHomeUser(item.userId)" @click="deletePost(item.id)">
                                            <Delete style="width: 1em; height: 1em; margin-right: 8px" />
                                            Delete post
                                        </el-dropdown-item>
                                        <el-dropdown-item v-if="!isHomeUser(item.userId)" @click="reportPost(item.id)">
                                            <WarnTriangleFilled style="width: 1em; height: 1em; margin-right: 8px" />
                                            Report post
                                        </el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                        </div>
                        <!-- content Post -->
                        <p style="white-space: pre-wrap;">{{ item.content }}</p>
                        <!-- content Post Share -->
                        <div v-if="item.postShare != null">
                            <div class="card">
                                <div class="card-body">
                                    <div class="d-flex align-items-center mb-3">
                                        <PopOver :user="item.postShare" :mapFriendStatus="mapFriendStatus" />
                                        <div class="flex-fill ps-2">
                                            <div class="fw-bold">
                                                <a :href="'/profile/' + item.postShare.userId"
                                                    class="text-decoration-none">
                                                    {{ item.postShare.firstName }} {{ item.postShare.lastName }}
                                                </a>
                                            </div>
                                            <a :href="'/post/' + item.postShare.id"
                                                class="small text-body text-opacity-50">
                                                {{ formatDate(item.postShare.createdDatetime) }}
                                            </a>
                                        </div>
                                    </div>

                                    <p style="white-space: pre-wrap;">{{ item.postShare.content }}</p>
                                    <div v-if="item.postShare.medias.length != 0" class="profile-img-list"
                                        style="margin: -0.2rem; margin-right: 8px;">
                                        <div v-for="(item2, index) in item.postShare.medias.slice(0, 5)" :key="item2"
                                            :class="'profile-img-list-item ' + numImage(index + 1, item.postShare.medias.length)">
                                            <a @click.prevent="zoomImage(item.postShare.medias, index, item)" data-lity
                                                class="profile-img-list-link"
                                                :style="'margin-top: 1x; margin-right: 1px;' + ((item.postShare.medias.length == 1 && item2.contentType.startsWith('video')) ? '' : ((item.postShare.medias.length == 1 && item2.contentType.startsWith('image')) ? 'max-height: 850px;' : ''))">
                                                <div class="profile-img-content">
                                                    <img v-if="item2.contentType.startsWith('image')"
                                                        :src="'data:' + item2.contentType + ';base64,' + item2.base64String"
                                                        alt=""
                                                        :style="(numImage(index + 1, item.postShare.medias.length) == 'one' && item.postShare.medias.length > 1) ? 'max-height:400px' : ''">
                                                    <video v-if="item2.contentType.startsWith('video')"
                                                        :src="'data:' + item2.contentType + ';base64,' + item2.base64String"
                                                        controls></video>
                                                </div>
                                                <div v-if="index == 4 && item.postShare.medias.length > 5"
                                                    class="profile-img-number">
                                                    +{{ item.postShare.medias.length - 5 }}</div>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- content Post -->
                        <div v-if="item.medias.length != 0" class="profile-img-list"
                            style="margin: -0.2rem; margin-right: 8px;">
                            <div v-for="(item2, index) in item.medias.slice(0, 5)" :key="item2"
                                :class="'profile-img-list-item ' + numImage(index + 1, item.medias.length)">
                                <a @click.prevent="zoomImage(item.medias, index, item)" class="profile-img-list-link"
                                    :style="'margin-top: 1x; margin-right: 1px;' + ((item.medias.length == 1 && item2.contentType.startsWith('video')) ? '' : ((item.medias.length == 1 && item2.contentType.startsWith('image')) ? 'max-height: 850px;' : ''))">
                                    <div class="profile-img-content">
                                        <img v-if="item2.contentType.startsWith('image')"
                                            :src="'data:' + item2.contentType + ';base64,' + item2.base64String" alt=""
                                            :style="(numImage(index + 1, item.medias.length) == 'one' && item.medias.length > 1) ? 'max-height:400px' : ''">
                                        <video v-if="item2.contentType.startsWith('video')"
                                            :src="'data:' + item2.contentType + ';base64,' + item2.base64String"
                                            controls></video>
                                    </div>
                                    <div v-if="index == 4 && item.medias.length > 5" class="profile-img-number">
                                        +{{ item.medias.length - 5 }}</div>
                                </a>
                            </div>
                        </div>
                        <!-- text people liked -->
                        <a style="cursor: pointer;" @click.prevent="callListLiked(item.listLiked)"
                            v-if="item.listLiked.length > 0" data-bs-target="#exampleModalLiked"
                            class="signup-image-link">
                            <i class="fa-solid fa-thumbs-up me-1 d-block d-sm-inline"></i>
                            {{ textCountLike(item.listLiked) }}
                        </a>
                        <hr class="mb-1 opacity-1" />
                        <!-- interact with posts options -->
                        <div class="row text-center fw-bold">
                            <div class="col">
                                <a @click.prevent="likePost(item.id, item.userId, index)"
                                    class="text-body text-opacity-50 text-decoration-none d-block p-2"
                                    style="cursor: pointer;">
                                    <span :style="item.liked ? 'color: Dodgerblue;' : ''">
                                        <i class="far fa-thumbs-up me-1 d-block d-sm-inline"></i>
                                        Likes
                                    </span>
                                </a>
                            </div>
                            <div class="col">
                                <a href="#" class="text-body text-opacity-50 text-decoration-none d-block p-2">
                                    <i class="far fa-comment me-1 d-block d-sm-inline"></i> Comment </a>
                            </div>
                            <div class="col">
                                <a @click.prevent="sharePost(item)" style="cursor: pointer;"
                                    class="text-body text-opacity-50 text-decoration-none d-block p-2">
                                    <i class="fa fa-share me-1 d-block d-sm-inline"></i> Share </a>
                            </div>
                        </div>
                        <hr class="mt-1 opacity-1" />
                        <!-- wirte comment -->
                        <el-scrollbar max-height="400px" view-style="overflow-x:hidden">
                            <div v-for="(item3, index) in item.listComment" class="d-flex align-items-start"
                                style="margin-bottom: 15px; margin-left: 10px">
                                <PopOver :user="item3" :mapFriendStatus="mapFriendStatus" />
                                <div class="flex-fill ps-2">
                                    <div class="position-relative d-flex align-items-center">
                                        <div class="card"
                                            style="max-width: 510px; margin-right: 10px; border-radius: 15px;">
                                            <div class="card-body" style="margin-top: -12px; margin-bottom: -12px;">
                                                <a :href="'/profile/' + item3.userId" class="text-decoration-none">
                                                    <b>{{ item3.firstName }} {{ item3.lastName }}</b>
                                                </a>
                                                <p style="white-space: pre-wrap;">{{ item3.commentText }}</p>

                                            </div>
                                        </div>
                                    </div>
                                    <div class="small text-body text-opacity-50">{{
                                        formatRelativeTime(item3.commentDate) }}
                                    </div>
                                </div>
                            </div>
                        </el-scrollbar>

                        <div class="d-flex align-items-start">
                            <img :src="(user.avatar == null) ? unknowAvatar : 'data:image/*;base64,' + user.avatar"
                                alt="" width="35" height="35" class="rounded-circle" />
                            <div class="flex-fill ps-2">
                                <div
                                    :class="'position-relative d-flex ' + ((isTextAreaExpanded) ? 'align-items-start' : 'align-items-center')">
                                    <textarea type="text" :id="'commentTextarea ' + item.id"
                                        :class="'form-control' + ((isTextAreaExpanded) ? ' ' : ' rounded-pill ') + 'bg-white bg-opacity-15'"
                                        @input="adjustTextareaHeight(item.id)"
                                        @keydown.enter.exact.prevent="addComment(item, index)"
                                        oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                                        style="padding-right: 120px; overflow: hidden; height: 10px;"
                                        placeholder="Write a comment..." />
                                    <div class="position-absolute end-0 text-center">
                                        <input type="submit"
                                            style="height: 0px; width: 0px; border: none; padding: 0px;"
                                            hidefocus="true" />
                                        <el-popover width="auto" trigger="click"
                                            popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding:0">
                                            <template #reference>
                                                <a @click="indexComment = item.id"
                                                    class="text-body text-opacity-50 me-2" style="cursor: pointer;"><i
                                                        class="fa fa-smile"></i></a>
                                            </template>
                                            <template #default>
                                                <EmojiPicker :native="true" @select="onSelectEmoji" />
                                            </template>
                                        </el-popover>
                                        <a href="#" class="text-body text-opacity-50 me-2"><i
                                                class="fa fa-camera"></i></a>
                                        <a href="#" class="text-body text-opacity-50 me-2"><i
                                                class="fa fa-video"></i></a>
                                        <a @click="addComment(item, index)" id="paw"
                                            class="text-body text-opacity-50 me-3"><i class="fa fa-paw"></i></a>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </template>
    </el-skeleton>

</template>

<script>
import DetailMedia from "../Detail_Media/DetailMedia.vue";
import "https://code.jquery.com/jquery-1.10.2.min.js";
import "https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js";
import postService from '@/services/post.service';
import userService from '@/services/user.service';
import reportService from '@/services/report.service';
import notifiService from '@/services/notification.service';
import friendService from '@/services/friend.service';
import { ElMessage } from 'element-plus';
import PopOver from "../PopOver/PopOver.vue";
import EmojiPicker from 'vue3-emoji-picker';
import 'vue3-emoji-picker/css';



export default {
    name: 'ListPost',
    components: {
        DetailMedia,
        PopOver,
        EmojiPicker
    },
    props: {
        posts: {
            type: Array,
        },
        loading: {
            type: Boolean,
        },
        friend: {
            type: Object
        }
    },
    watch: {
        friend: function (newValue, oldValue) {
            if (newValue.content != " is now your friend.") {
                delete this.mapFriendStatus.friendStatusSender[newValue.senderId];
                this.mapFriendStatus.friendStatusReceiver[newValue.senderId] = newValue.status;
            }

            else
                this.mapFriendStatus.friendStatusSender[newValue.senderId] = newValue.status;
        },
    },
    data() {
        return {
            stompClient: null,
            received_messages: [],
            send_message: null,
            connected: false,
            report: {
                reportTypeId: null,
                postId: null,
                reason: null
            },
            response: {
                status: "",
                message: "",
                result: null
            },
            user: {},
            notifications: [],
            listReportType: [],
            selectReportType: null,
            postRequest: {
                postId: null,
                userId: "",
                content: null,
                idDeleted: [],
                sharedId: null
            },
            delete: new Set(),
            postEdit: {
                content: null,
                media: [],
                postId: null
            },
            postShare: {
                postId: null,
                userId: null,
                avatar: null,
                firstName: null,
                lastName: null,
                createdDatetime: null,
                medias: [],
                content: null
            },
            media: [],
            mediaDeleted: [],
            fileSelectedToAdd: [],
            listLiked: [],
            comment: {
                postId: null,
                commentText: null
            },
            userDetailMedia: {
                firstName: "",
                lastName: "",
                createdDatetime: "01/02/2003"
            },
            isTextAreaExpanded: false,
            isShare: false,
            listMedia: [],
            indexMedia: null,
            mapFriendStatus: null,
            indexComment: null,
            unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
        };
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
        }
    },
    mounted() {
        this.getUserInfo();
        this.getMapFriendStatus();
    },
    methods: {
        onSelectEmoji(emoji) {
            document.getElementById("commentTextarea " + this.indexComment).value = document.getElementById("commentTextarea " + this.indexComment).value + emoji.i
        },
        async getMapFriendStatus() {
            await friendService.getStatus().then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            if (this.response.status == "SUCCESS") {
                this.mapFriendStatus = this.response.result;
            }

        },
        async addFriend(receiverId) {
            await friendService.addFriend(receiverId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            if (this.response.status == "SUCCESS") {
                ElMessage({
                    message: this.response.message,
                    type: 'success',
                });
            }
            else {
                ElMessage.error(this.response.message);
            };
        },
        async addNotifi(receiverId, postId, type) {
            var notificatioReponse = {
                type: type,
                receiverId: receiverId,
                postId: postId
            }
            await notifiService.addNotifi(notificatioReponse).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );

        },
        reportPost(postId) {
            this.report.postId = postId;
            this.getListTypeReport();
            $('#exampleModalReport').modal('toggle');
        },
        async submitReport() {
            this.report.reportTypeId = this.selectReportType;
            await reportService.reportPost(this.report).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            if (this.response.status == "SUCCESS") {
                this.report.reason = null;
                this.selectReportType = null;
                $('#exampleModalReport').modal('toggle');
                ElMessage({
                    message: this.response.message,
                    type: 'success',
                });
            }
            else {
                ElMessage.error(this.response.message);
            };

        },
        async getListTypeReport() {
            await reportService.getTypeReport().then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response

                }
            );

            if (this.response.status == "SUCCESS") {
                this.listReportType = this.response.result;
            }

        },
        zoomImage(listMedia, index, userDetailMedia) {
            this.userDetailMedia = userDetailMedia;
            this.listMedia = listMedia;
            this.indexMedia = index;
            this.$refs.detailMedia.setActiveItem();
            $('#exampleModalPost').modal('toggle');
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
        sharePost(post) {
            if (post.postShare == null) {
                this.postShare.postId = post.id
                this.postShare.userId = post.userId;
                this.postShare.avatar = post.avatar;
                this.postShare.firstName = post.firstName;
                this.postShare.lastName = post.lastName;
                this.postShare.createdDatetime = post.createdDatetime;
                this.postShare.medias = post.medias;
                this.postShare.content = post.content;
                $('#exampleModalShare').modal('toggle');
            }
            else {
                this.postShare.postId = post.postShare.id
                this.postShare.userId = post.postShare.userId;
                this.postShare.avatar = post.postShare.avatar;
                this.postShare.firstName = post.postShare.firstName;
                this.postShare.lastName = post.postShare.lastName;
                this.postShare.createdDatetime = post.postShare.createdDatetime;
                this.postShare.medias = post.postShare.medias;
                this.postShare.content = post.postShare.content;
                $('#exampleModalShare').modal('toggle');
            }
        },
        async addComment(item, index) {
            var postId = item.id;
            var textareaValue = document.getElementById("commentTextarea " + postId).value;
            if (textareaValue.length > 0) {
                this.comment.commentText = textareaValue;
                this.comment.postId = postId;
                await postService.commentPost(this.comment).then(
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
                    this.comment.commentText = null;
                    this.comment.postId = null;
                    const user = {
                        avatar: this.user.avatar,
                        firstName: this.user.firstName,
                        lastName: this.user.lastName,
                        commentDate: new Date(),
                        commentText: textareaValue
                    };
                    this.sortedArray[index].listComment.push(user);
                    document.getElementById("commentTextarea " + postId).value = null;
                    this.adjustTextareaHeight(item.id);
                    if (item.userId != this.$store.state.auth.user.id)
                        this.addNotifi(item.userId, item.id, "comment");

                }

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
        },
        classPost() {
            if (this.$route.path.includes("/profile/"))
                return "col-md-12 grid-margin";
            else
                return "col-md-6 offset-md-3";
        },
        formatDate(timestamp) {
            const date = new Date(timestamp);
            const options = {
                day: 'numeric',
                month: 'long',
                hour: 'numeric',
                minute: 'numeric'
            };

            const yearOption = {
                day: 'numeric',
                month: 'long',
                year: 'numeric',
                hour: 'numeric',
                minute: 'numeric'
            };

            const currentYear = new Date().getFullYear();

            if (date.getFullYear() === currentYear) {
                return new Intl.DateTimeFormat('en-EN', options).format(date);
            } else {
                return new Intl.DateTimeFormat('en-EN', yearOption).format(date);
            }
        },
        adjustTextareaHeight(postId) {
            const textarea = document.getElementById("commentTextarea " + postId);
            if (textarea.scrollHeight > 58)
                this.isTextAreaExpanded = true
            if (textarea.scrollHeight <= 58 || textarea.value == '') {
                this.isTextAreaExpanded = false;
                textarea.style.height = "10px";
            }
        },
        callListLiked(listLiked) {
            this.listLiked = listLiked
            $('#exampleModalLiked').modal('toggle');
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
        async likePost(postId, receiverId, index) {
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
                    userId: this.$store.state.auth.user.id,
                    avatar: (this.user.avatar == this.unknowAvatar) ? null : this.user.avatar,
                    firstName: this.user.firstName,
                    lastName: this.user.lastName,
                };
                if (this.response.result) {
                    this.posts[index].listLiked.unshift(userInfo);
                    if (receiverId != this.$store.state.auth.user.id)
                        this.addNotifi(receiverId, postId, "like")
                } else
                    this.posts[index].listLiked.shift();
            }

        },
        handleFileUploadEdit() {
            this.$refs.fileInput123.click();
        },

        handleFileSelectedEdit(event) {
            for (let i = 0; i < event.target.files.length; i++) {
                this.postEdit.media.push(event.target.files[i]);
                this.fileSelectedToAdd.push(event.target.files[i]);
            }
            event.target.value = '';
        },
        isFile(item) {
            return item instanceof File;
        },
        createURL(itemFile) {
            return URL.createObjectURL(itemFile);
        },
        async sumbitPost(sharedPostId = null) {
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
                    this.postShare.postId = null;
                    this.media = [];
                    ElMessage({
                        message: this.response.message,
                        type: 'success',
                    });
                    if (sharedPostId == null)
                        $('#exampleModal').modal('toggle');
                    else
                        $('#exampleModalShare').modal('toggle');
                    this.sortedArray.unshift(this.response.result);
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
        replaceWithBr(text) {
            if (text != null)
                return text.replace(/\n/g, "<br />");
            else
                return text;
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

                }
            );
            if (this.response.status == "SUCCESS") {
                this.delete.add(postId);
                ElMessage({
                    message: this.response.message,
                    type: 'success',
                });
            }
            else {
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
                }
            );

            if (this.response.status == "SUCCESS") {
                this.delete.delete(postId);
                ElMessage({
                    message: this.response.message,
                    type: 'success',
                });
            }
            else {
                ElMessage.error(this.response.message);
            };
        },
        isDelete(postId) {
            return this.delete.has(postId);
        },
        isHomeUser(userId) {
            return userId == this.$store.state.auth.user.id;
        },
        editPost(post) {
            if (post.postShare != null) {
                this.isShare = true;
                this.postEdit.content = post.content;
                this.postEdit.postId = post.id;
                this.postShare.postId = post.postShare.id;
                this.postShare.userId = post.postShare.userId;
                this.postShare.avatar = post.postShare.avatar;
                this.postShare.firstName = post.postShare.firstName;
                this.postShare.lastName = post.postShare.lastName;
                this.postShare.createdDatetime = post.postShare.createdDatetime;
                this.postShare.medias = post.postShare.medias;
                this.postShare.content = post.postShare.content;
                $('#exampleModalEdit').modal('toggle');
            } else {
                this.isShare = false;
                this.postEdit.media = post.medias;
                this.postEdit.content = post.content;
                this.postEdit.postId = post.id;
                $('#exampleModalEdit').modal('toggle');
            }

        },
        async sumbitEditPost(postId) {
            if (this.postEdit.content != null && this.postEdit.content != "" || this.fileSelectedToAdd.length != 0 || this.postEdit.media.length != 0) {

                this.postRequest.userId = this.$store.state.auth.user.id;
                this.postRequest.content = this.postEdit.content;
                this.postRequest.postId = postId;
                this.postRequest.idDeleted = this.mediaDeleted;
                this.postRequest.sharedId = (this.isShare) ? this.postShare.postId : null;

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
                    this.postRequest.content = null;
                    this.isShare = false;
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

        }
    }
}
</script>

<style scoped>
@import url(./ListPost.css);
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css);
@import url(https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css);
</style>