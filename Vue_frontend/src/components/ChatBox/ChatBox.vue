<template>
    <div v-if="!isClose" class="col-md-8 col-lg-6 col-xl-4">

        <div class="card" id="chat1" style="border-radius: 15px; ">
            <div class="card-header d-flex justify-content-between align-items-center p-3 bg-info text-white border-bottom-0"
                style="border-top-left-radius: 15px; border-top-right-radius: 15px;">
                <div v-if="friend.status == 'ONLINE'" class="green-dot"></div>
                <div v-else class="gray-dot"></div>
                <div class="d-flex align-items-center">
                    <el-avatar :size="30" style="margin-right: -1px;"
                        :src="(friend.avatar != null) ? 'data:image/*;base64,' + friend.avatar : unknowAvatar" />
                    <a class="flex-fill ps-2" id="friendName" :href="'/profile/' + friend.userId">
                        <span style="margin-left: 3px;" class="mb-0 fw-bold">
                            {{ friend.firstName + " " + friend.lastName }}
                        </span>
                    </a>
                </div>
                <i @click="closeBox" class="fas fa-times"></i>
            </div>
            <div class="card-body">
                <el-scrollbar ref="scrollbar" height="394px" view-style="overflow-x:hidden,">
                    <div v-for="(item, index) in listChat" :key="item"
                        :class="'d-flex flex-row ' + positionBubble(item.senderId) + ' mb-4'"
                        style="margin-right: 25px;">
                        <div v-if="item.senderId != friend.userId" class="p-3 me-3 border"
                            style="border-radius: 15px; background-color: #fbfbfb; max-width: 300px;">
                            <p style="white-space: pre-wrap; " class="small mb-0">{{ item.message }}</p>
                        </div>
                        <el-avatar v-if="item.senderId != friend.userId" :size="60" :fit="'fill'"
                            style="height: 45px; width: 45px;"
                            :src="(userAvatar != null) ? 'data:image/*;base64,' + userAvatar : unknowAvatar" />
                        <el-avatar v-if="item.senderId == friend.userId" :size="60" :fit="'fill'"
                            style="height: 45px; width: 45px;"
                            :src="(friend.avatar != null) ? 'data:image/*;base64,' + friend.avatar : unknowAvatar" />
                        <div v-if="item.senderId == friend.userId" class="p-3 ms-3"
                            style="border-radius: 15px; background-color: rgba(57, 192, 237,.2); max-width: 300px;">
                            <p style="white-space: pre-wrap; " class="small mb-0">{{ item.message }}</p>
                        </div>
                    </div>
                </el-scrollbar>

                <div class="form-outline">
                    <div
                        :class="'position-relative d-flex ' + ((isTextAreaExpanded) ? 'align-items-start' : 'align-items-center')">
                        <textarea type="text" id="chatTextarea"
                            :class="'form-control' + ((isTextAreaExpanded) ? ' ' : ' rounded-pill ') + 'bg-white bg-opacity-15'"
                            @input="adjustTextareaHeight()" @keydown.enter.exact.prevent="sendMessage()"
                            oninput='this.style.height = "10px";this.style.height = this.scrollHeight + "px"'
                            style="padding-right: 120px; height: 10px; max-height: 100px;"
                            placeholder="Type your message" />
                        <div class="position-absolute end-0 text-center">
                            <input type="submit" style="height: 0px; width: 0px; border: none; padding: 0px;"
                                hidefocus="true" />
                            <el-popover width="auto" trigger="click"
                                popper-style="box-shadow: rgb(14 18 22 / 35%) 0px 10px 38px -10px, rgb(14 18 22 / 20%) 0px 10px 20px -15px; padding:0">
                                <template #reference>
                                    <a href="#" class="text-body text-opacity-50 me-2"><i class="fa fa-smile"></i></a>
                                </template>
                                <template #default>
                                    <EmojiPicker :native="true" :disable-skin-tones="true"
                                        :disable-sticky-group-names="true" @select="onSelectEmoji" />
                                </template>
                            </el-popover>

                            <a href="#" class="text-body text-opacity-50 me-2"><i class="fa fa-camera"></i></a>
                            <a href="#" class="text-body text-opacity-50 me-2"><i class="fa fa-video"></i></a>
                            <a @click="sendMessage()" class="text-body text-opacity-50 me-3" style="cursor: pointer;"><i
                                    class="fa fa-paw"></i></a>
                        </div>
                    </div>

                </div>

            </div>
        </div>

    </div>
</template>
<script>
import chatService from '@/services/chat.service';
import EmojiPicker from 'vue3-emoji-picker';
import 'vue3-emoji-picker/css';
export default {
    name: 'ChatBox',
    components: {
        EmojiPicker
    },
    props: {
        isClose: {
            type: Boolean,
        },
        friend: {
            type: Object,
        },
        userAvatar: {
            type: String,
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
            txtSearch: "",
            isTextAreaExpanded: false,
            isClose2: true,
            listChat: [],
            unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
        }

    },
    mounted() {

    },
    methods: {
        onSelectEmoji(emoji) {

            document.getElementById("chatTextarea").value = document.getElementById("chatTextarea").value + emoji.i
        },
        setScrollTo() {
            setTimeout(() => {
                const scrollbar = this.$refs.scrollbar;
                const scrollbarViewHeight = scrollbar.$el.querySelector('.el-scrollbar__view').clientHeight;
                scrollbar.setScrollTop(scrollbarViewHeight);
            }, 50);
        },
        addMessage(message) {
            var chatTextMedia = {};
            chatTextMedia = JSON.parse(message);

            if (chatTextMedia.chatTextResponse != null)
                this.listChat.push(chatTextMedia.chatTextResponse)
            if (chatTextMedia.chatMediaResponse != null)
                this.listChat.push(chatTextMedia.chatMediaResponse)
            if (chatTextMedia.chatTextResponse.senderId != this.$store.state.auth.user.id)
                this.$emit('sendUserId', chatTextMedia.chatTextResponse.senderId);
        },
        positionBubble(senderId) {
            switch (senderId) {
                case this.friend.userId:
                    return 'justify-content-start';

                default:
                    return 'justify-content-end';
            }
        },
        async getHistory(receiverId) {

            await chatService.getHistory(receiverId).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            if (this.response.status == "SUCCESS") {
                this.listChat = this.response.result;
            }
        },
        async sendMessage() {
            var textareaValue = document.getElementById("chatTextarea").value;
            var receverMessage = {
                receiverId: this.friend.userId,
                message: textareaValue
            }
            const formData = new FormData();
            formData.append('chatMessage', JSON.stringify(receverMessage));
            formData.append('media', null);
            await chatService.sendMessage(formData).then(
                (response) => {
                    this.response = response.data;
                },
                (error) => {
                    this.response =
                        error.response
                }
            );
            if (this.response.status == "SUCCESS") {
                document.getElementById("chatTextarea").value = '';
                this.adjustTextareaHeight();
                // this.listChat.push(this.response.result.chatTextResponse);
                // setTimeout(() => {
                //     const scrollbar = this.$refs.scrollbar;
                //     const scrollbarViewHeight = scrollbar.$el.querySelector('.el-scrollbar__view').clientHeight;
                //     scrollbar.setScrollTop(scrollbarViewHeight);
                // }, 50);

            }
        },
        closeBox() {
            this.$emit('changeClose', true);
            this.$emit('sendUserId', null);
        },
        adjustTextareaHeight() {
            if (document.getElementById("chatTextarea").scrollHeight > 58)
                this.isTextAreaExpanded = true
            if (document.getElementById("chatTextarea").scrollHeight <= 58 || document.getElementById("chatTextarea").value == '') {
                this.isTextAreaExpanded = false;
                document.getElementById("chatTextarea").style.height = '10px'
            }
        },
        isFile(item) {
            return item instanceof File;
        },
        createURL(itemFile) {
            return URL.createObjectURL(itemFile);
        },
    }
}
</script>
<style scoped>
@import url(./ChatBox.css);
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css);
</style>