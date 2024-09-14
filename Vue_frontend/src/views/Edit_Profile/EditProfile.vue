<template>

    <body>
        <div class="container-xl px-4 mt-0" style="padding-top: 80px;">

            <nav class="nav nav-borders">
                <a class="nav-link active ms-0" href="/edit" target="__blank">Profile</a>
                <a class="nav-link" href="/change-password" target="__blank">Security</a>
            </nav>
            <hr class="mt-0 mb-4">
            <div class="row">
                <div class="col-xl-4">

                    <div class="card mb-4 mb-xl-0">
                        <div class="card-header">Profile Picture</div>
                        <div class="card-body text-center">

                            <img class="img-account-profile rounded-circle mb-2"
                                style="max-height:160px;max-width: 160px;" :src="user.avatar" alt>

                            <div class="small font-italic text-muted mb-4">JPG or PNG no larger than 5 MB</div>
                            <input ref="fileInput123" style="display: none" type="file" accept="image/*"
                                @change="handleFileSelected" />
                            <button class="btn btn-primary" @click="handleFileUpload" type="button">Upload new
                                image</button>
                        </div>
                    </div>
                </div>
                <div class="col-xl-8">

                    <div class="card mb-4">
                        <div class="card-header">Account Details</div>
                        <div class="card-body">
                            <form>

                                <div class="row gx-3 mb-3">

                                    <div class="col-md-6">
                                        <label class="small mb-1" for="inputFirstName">First name</label>
                                        <input v-model="user.firstName" class="form-control" id="inputFirstName"
                                            type="text" placeholder="Enter your first name">
                                        <small id="firstnameNotValid" class="text-danger"></small>
                                    </div>

                                    <div class="col-md-6">
                                        <label class="small mb-1" for="inputLastName">Last name</label>
                                        <input v-model="user.lastName" class="form-control" id="inputLastName"
                                            type="text" placeholder="Enter your last name">
                                        <small id="lastnameNotValid" class="text-danger"></small>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label class="small mb-1" for="inputUsername">Address</label>
                                    <input v-model="user.address" class="form-control" id="inputUsername" type="text"
                                        placeholder="Enter your address">
                                    <small id="addressNotValid" class="text-danger"></small>
                                </div>

                                <div class="mb-3">
                                    <label class="small mb-1" for="inputEmailAddress">Email address</label>
                                    <input v-model="user.email" class="form-control" id="inputEmailAddress" type="email"
                                        placeholder="Enter your email address">
                                    <small id="emailNotValid" class="text-danger"></small>
                                </div>

                                <div class="row gx-3 mb-3">

                                    <div class="col-md-6">
                                        <label class="small mb-1" for="inputPhone">Phone number</label>
                                        <input v-model="user.phone" class="form-control" id="inputPhone" type="tel"
                                            placeholder="Enter your phone number">
                                        <small id="phoneNotValid" class="text-danger"></small>
                                    </div>

                                    <div class="col-md-6">
                                        <label class="small mb-1" for="inputBirthday">Birthday</label>
                                        <input v-model="user.birthday" class="form-control" id="inputBirthday"
                                            type="date" name="birthday" placeholder="Enter your birthday">
                                        <small id="birthdayNotValid" class="text-danger"></small>
                                    </div>
                                </div>
                                <button :class="{ 'disabled': !isDirty }" class="btn btn-primary" @click="editUser"
                                    type="button">Save
                                    changes</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</template>
<script>
import "https://code.jquery.com/jquery-1.10.2.min.js";
import userService from "@/services/user.service";
import { ElMessage, ElMessageBox } from 'element-plus'
export default {
    name: 'TimeLine',
    data() {
        return {
            user: {
                userId: "",
                firstName: "",
                lastName: "",
                avatar: "",
                introduce: null,
                joined: null,
                address: "",
                phone: null,
                gender: 1,
                birthday: null,
                email: null
            },
            initialUser: {},
            dirty: false,
            valid: false,
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
    mounted() {
        this.getUserInfo();
    },
    methods: {
        isValidEmail(email) {
            const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return regex.test(email);
        },
        validInfo() {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            const birthdayRegex = /^\d{4}-\d{2}-\d{2}$/;
            const phoneRegex = /^[0-9]{10,}$/; // Ít nhất 10 chữ số

            this.valid = true;
            // Xác thực email
            if (!emailRegex.test(this.user.email)) {
                document.getElementById("emailNotValid").innerHTML = 'Please enter a valid email address.';
                this.valid = false
            } else {
                document.getElementById("emailNotValid").innerHTML = "";
            }

            // Xác thực birthday
            if (!birthdayRegex.test(this.user.birthday)) {
                document.getElementById("birthdayNotValid").innerHTML = 'Please enter a valid birthday.';
                this.valid = false
            } else {
                document.getElementById("birthdayNotValid").innerHTML = "";
            }

            // Xác thực firstname
            if (!this.user.firstName.trim()) {
                document.getElementById("firstnameNotValid").innerHTML = 'Please enter your first name.';
                this.valid = false
            } else {
                document.getElementById("firstnameNotValid").innerHTML = "";
            }

            // Xác thực lastname
            if (!this.user.lastName.trim()) {
                document.getElementById("lastnameNotValid").innerHTML = 'Please enter your last name.';
                this.valid = false
            } else {
                document.getElementById("lastnameNotValid").innerHTML = "";
            }

            // Xác thực address
            if (!this.user.address.trim()) {
                document.getElementById("addressNotValid").innerHTML = 'Please enter your address.';
                this.valid = false
            } else {
                document.getElementById("addressNotValid").innerHTML = "";
            }

            // Xác thực phone
            if (!phoneRegex.test(this.user.phone)) {
                document.getElementById("phoneNotValid").innerHTML = 'The phone number must be numeric and at least 10 digits long.';
                this.valid = false
            } else {
                document.getElementById("phoneNotValid").innerHTML = "";
            }
        },
        async getUserInfo() {
            await userService.getUser(this.$store.state.auth.user.id).then(
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

            this.user = this.response.result;
            this.initialUser = { ...this.user };
            if (this.user.avatar == null)
                this.user.avatar = this.unknowAvatar;
            else
                this.user.avatar = "data:image/*;base64," + this.user.avatar;
        },
        handleFileUpload() {
            this.$refs.fileInput123.click();
        },

        handleFileSelected(event) {
            this.changeAvatar(event.target.files[0]);
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

                        }
                    );
                    this.getUserInfo();
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
        async editUser() {
            this.validInfo()
            if (this.valid) {
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
                            }
                        );
                        if (this.response.status == "SUCCESS") {
                            this.initialUser = { ...this.user };
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
    },
}
</script>
<style scoped>
@import url(./EditProfile.css);
</style>