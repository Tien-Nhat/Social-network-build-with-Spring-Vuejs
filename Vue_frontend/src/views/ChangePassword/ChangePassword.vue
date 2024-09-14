<template>

    <body>
        <div class="container-xl px-4 mt-0" style="padding-top: 80px;">
            <!-- Account page navigation-->
            <nav class="nav nav-borders">
                <a class="nav-link  ms-0" href="/edit" target="__blank">Profile</a>
                <a class="nav-link active" href="/change-pasword" target="__blank">Security</a>
            </nav>
            <hr class="mt-0 mb-4">
            <div class="col-lg">
                <!-- Change password card-->
                <div class="card mb-4">
                    <div class="card-header">Change Password</div>
                    <div class="card-body">
                        <form>
                            <!-- Form Group (current password)-->
                            <div class="mb-3">
                                <label class="small mb-1" for="currentPassword">Current Password</label>
                                <input v-model="passwordRequest.oldPass" class="form-control" id="currentPassword"
                                    type="password" placeholder="Enter current password">
                                <small id="oldPassNotValid" class="text-danger"></small>
                            </div>
                            <!-- Form Group (new password)-->
                            <div class="mb-3">
                                <label class="small mb-1" for="newPassword">New Password</label>
                                <input v-model="passwordRequest.newPass" class="form-control" id="newPassword"
                                    type="password" placeholder="Enter new password">
                                <small id="newPassNotValid" class="text-danger"></small>
                            </div>
                            <!-- Form Group (confirm password)-->
                            <div class="mb-3">
                                <label class="small mb-1" for="confirmPassword">Confirm Password</label>
                                <input v-model="repeatPassword" class="form-control" id="confirmPassword"
                                    type="password" placeholder="Confirm new password">
                                <small id="repeatPassNotValid" class="text-danger"></small>
                            </div>
                            <button class="btn btn-primary" @click="changePassword" type="button">Save</button>
                        </form>
                    </div>
                </div>

            </div>
        </div>
    </body>

</template>
<script>
import userService from "@/services/user.service";
import { ElMessage, ElMessageBox } from 'element-plus'
export default {
    name: 'TimeLine',
    data() {
        return {
            passwordRequest: {
                oldPass: "",
                newPass: ""
            },
            initialUser: {},
            dirty: false,
            valid: false,
            repeatPassword: null,
            unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
        }
    },
    computed: {
        isDirty() {
            return Object.keys(this.user).some(
                key => {
                    if (key === "avatar") {
                        return false;
                    }
                    return this.user[key] !== this.initialUser[key];
                }
            );
        }
    },
    methods: {
        validPassword() {
            const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+}{":;'?/>.<,])(?=.*[^\s]).{7,}$/;

            this.valid = true; // Đặt mặc định là true, nếu có lỗi sẽ đặt lại là false

            if (!this.passwordRequest.oldPass.trim()) {
                document.getElementById("oldPassNotValid").innerHTML = 'Please enter your current password.';
                this.valid = false
            } else {
                document.getElementById("oldPassNotValid").innerHTML = "";
            }

            // Xác thực password
            if (!passwordRegex.test(this.passwordRequest.newPass)) {
                document.getElementById("newPassNotValid").innerHTML = 'Password must contain at least 8 characters, including at least one digit, one lowercase letter, one uppercase letter, one special character, and no spaces.';
                this.valid = false;
            } else {
                document.getElementById("newPassNotValid").innerHTML = "";
            }

            if (this.passwordRequest.newPass !== this.repeatPassword) {
                document.getElementById("repeatPassNotValid").innerHTML = 'Passwords do not match.';
                this.valid = false;
            } else {
                document.getElementById("repeatPassNotValid").innerHTML = "";
            }
        },
        async changePassword() {
            this.validPassword();
            if (this.valid) {
                ElMessageBox.confirm(
                    'Are you sure you want to change password ?',
                    'Warning',
                    {
                        confirmButtonText: 'OK',
                        cancelButtonText: 'Cancel',
                        type: 'warning',
                    })
                    .then(async () => {
                        await userService.changePass(this.passwordRequest).then(
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
            }
        }
    }
}
</script>

<style scoped>
@import url(./ChangePassword.css);
</style>