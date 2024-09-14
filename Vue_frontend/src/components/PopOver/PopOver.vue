<template>
    <el-popover placement="top-start" :width="300" trigger="hover"
        content="this is content, this is content, this is content">
        <template #reference>
            <a :href="'/profile/' + user.userId"><img
                    :src="(user.avatar != null) ? 'data:image/*;base64,' + user.avatar : unknowAvatar" alt="" width="30"
                    height="30" class="rounded-circle" /></a>
        </template>
        <template #default>
            <div class="demo-rich-conent" style="display: flex; gap: 16px; flex-direction: column">
                <div class="common-layout">
                    <el-container>
                        <el-aside width="140px"><el-avatar :size="60"
                                :src="(user.avatar != null) ? 'data:image/*;base64,' + user.avatar : unknowAvatar"
                                style="margin-bottom: 1px; margin-top: 60px;" /></el-aside>
                        <el-main v-if="!isHomeUser(user.userId)" style="overflow-x: hidden; width: 100px;">
                            <el-button v-if="getStatusFriend(user.userId) == 'NOT_FRIEND'"
                                @click="addFriend(user.userId)"
                                style="transform: scale(0.8); margin-bottom: -20px; margin-top: 70px; margin-left: 0px;"
                                type="primary">
                                <i class="fa fa-user-plus" aria-hidden="true" style="padding-right: 2px;" />
                                Add friend
                            </el-button>
                            <el-button v-if="mapFriendStatus.friendStatusSender[user.userId] == 'PENDING'"
                                @click="addFriend(user.userId)"
                                style="transform: scale(0.8); margin-bottom: -20px; margin-top: 70px; margin-left: 0px;"
                                type="primary">
                                <i class="fas fa-hourglass" aria-hidden="true" style="padding-right: 2px;" />
                                Cancel invite
                            </el-button>
                            <el-dropdown :teleported="false"
                                v-if="mapFriendStatus.friendStatusReceiver[user.userId] == 'PENDING'" trigger="click">
                                <el-button
                                    style="transform: scale(0.8); margin-bottom: -20px; margin-top: 70px; margin-left: 0px;"
                                    type="primary">
                                    <i class="fa fa-user-plus" aria-hidden="true" style="padding-right: 2px;" />
                                    Reply invite
                                </el-button>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item @click="acceptFriend(user.userId)"> <i
                                                class="fa-solid fa-check" aria-hidden="true"
                                                style="padding-right: 2px;" />
                                            Accept invite</el-dropdown-item>
                                        <el-dropdown-item @click="declineFriend(user.userId)"><i class="fa-solid fa-x"
                                                aria-hidden="true" style="padding-right: 2px;" />
                                            Decline invite
                                        </el-dropdown-item>

                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                            <el-dropdown :teleported="false" v-if="getStatusFriend(user.userId) == 'ACCEPTED'"
                                trigger="click">
                                <el-button
                                    style="transform: scale(0.8); margin-bottom: -20px; margin-top: 70px; margin-left: 0px;"
                                    type="primary">
                                    <i class="fa-solid fa-user-check" aria-hidden="true" style="padding-right: 2px;" />
                                    Friend
                                </el-button>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item @click="declineFriend(user.userId)"><i class="fa-solid fa-x"
                                                aria-hidden="true" style="padding-right: 2px;" />
                                            Unfriend
                                        </el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>

                        </el-main>
                    </el-container>
                </div>
                <el-image style="width: 270px; height: 100px; position: absolute; z-index: -1;"
                    src="https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg" fit="cover" />
                <div>
                    <p class="demo-rich-content__name" style="margin: 0; font-weight: 500">
                        {{ user.firstName + " " + user.lastName
                        }}
                    </p>
                    <p class="demo-rich-content__mention"
                        style="margin: 0; font-size: 14px; color: var(--el-color-info)">
                        @{{ user.userId }}
                    </p>
                </div>
                <p class="demo-rich-content__desc" style="margin: 0">
                    {{ user.introduce }}
                </p>
            </div>
        </template>
    </el-popover>
</template>
<script>
import friendService from '@/services/friend.service';
import { ElMessage } from 'element-plus';
import Stomp from "webstomp-client";
import SockJS from 'sockjs-client';
export default {
    name: 'PopOver',
    props: {
        user: {
            type: Object,
        },
        mapFriendStatus: {
            type: Object
        }
    },
    data() {
        return {
            unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
        }
    },
    mounted() {
        // this.connect();
    },
    methods: {

        isHomeUser(receiverId) {
            return this.$store.state.auth.user.id == receiverId;
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
            if (this.response.status == "SUCCESS")
                this.mapFriendStatus.friendStatusSender[receiverId] = this.response.result;
            else
                ElMessage.error(this.response.message);
        },
        formatRequestFriend(status) {
            switch (status) {
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
        changeIcon(status) {
            switch (status) {
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
        getStatusFriend(userId) {
            if (this.mapFriendStatus.friendStatusSender[userId] != null)
                return this.mapFriendStatus.friendStatusSender[userId];
            if (this.mapFriendStatus.friendStatusReceiver[userId] != null)
                return this.mapFriendStatus.friendStatusReceiver[userId];
            return "NOT_FRIEND";
        },
        async acceptFriend(userId) {
            await friendService.acceptFriend(userId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );

            if (this.response.status == "SUCCESS")
                this.mapFriendStatus.friendStatusReceiver[userId] = this.response.result;
            else
                ElMessage.error(this.response.message);
        },
        async declineFriend(userId) {
            await friendService.deleteFriend(userId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            if (this.response.status == "SUCCESS") {
                if (this.mapFriendStatus.friendStatusSender[userId] != null)
                    this.mapFriendStatus.friendStatusSender[userId] = this.response.result;
                if (this.mapFriendStatus.friendStatusReceiver[userId] != null)
                    this.mapFriendStatus.friendStatusReceiver[userId] = this.response.result;

            }

        },
    }
}
</script>