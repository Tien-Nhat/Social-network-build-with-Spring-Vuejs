<template>
    <div class="main">
        <section class="signup">
            <div v-if="isInfo" class="container">
                <div class="signup-content">
                    <div class="signup-form">

                        <form v-if="!successful" method="POST" @submit.prevent="handleRegister" class="register-form"
                            id="register-form">
                            <h2 class="form-title">Sign up</h2>
                            <div class="form-group">
                                <label for="name"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                <input type="text" name="name" id="name" placeholder="Username" v-model="user.username"
                                    required maxlength="20" />
                            </div>
                            <div class="form-group">
                                <label for="email"><i class="zmdi zmdi-email"></i></label>
                                <input type="email" name="email" id="email" placeholder="Your Email"
                                    v-model="user.email" required maxlength="30" />
                            </div>
                            <div class="form-group">
                                <label for="pass"><i class="zmdi zmdi-lock"></i></label>
                                <input type="password" name="pass" id="pass" placeholder="Password"
                                    v-model="user.password" required minlength="7" />
                            </div>
                            <div class="form-group">
                                <label for="re-pass"><i class="zmdi zmdi-lock-outline"></i></label>
                                <input type="password" name="re_pass" id="re_pass" placeholder="Repeat your password"
                                    v-model="repeatpass" required />
                            </div>
                            <div class="form-group form-button">
                                <input type="submit" name="signup" id="signup" class="form-submit" value="Register" />
                            </div>
                        </form>
                    </div>

                    <div class="signup-image">
                        <figure><img src="../../assets/images/signup-image.jpg" alt="sing up image"></figure>
                        <a href="/login" class="signup-image-link"><u>I am already member</u>.</a>
                    </div>
                </div>
            </div>
            <div v-else class="container">
                <div class="signup-content">
                    <div class="signup-form">

                        <form method="POST" @submit.prevent="checkInfo" class="register-form" id="register-form">
                            <h2 class="form-title">Info</h2>
                            <div class="form-group">
                                <label for="name"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                <input type="text" name="firstname" id="firstname" placeholder="First Name"
                                    v-model="user.firstname" required maxlength="20" />
                            </div>
                            <div class="form-group">
                                <label for="lastname"><i class="zmdi zmdi-account material-icons-name"></i></label>
                                <input type="text" name="lastname" id="lastname" placeholder="Last Name"
                                    v-model="user.lastname" required maxlength="30" />
                            </div>
                            <div class="form-group">
                                <label for="birthday"><i class="zmdi zmdi-calendar material-icons-name"></i></label>
                                <input type="date" name="birthday" id="birthday" placeholder="Birthday"
                                    v-model="user.birthday" required />
                            </div>
                            <div class="p-t-10">
                                <label class="radio-container m-r-45">Male
                                    <input type="radio" value=0 checked="checked" v-model="user.gender" name="gender">
                                    <span class="checkmark"></span>
                                </label>
                                <label class="radio-container">Female
                                    <input type="radio" value=1 v-model="user.gender" name="gender">
                                    <span class="checkmark"></span>
                                </label>
                            </div>
                            <div class="form-group">
                                <label for="address"><i class="zmdi zmdi-home material-icons-name"></i></label>
                                <input type="text" name="address" id="address" placeholder="Address"
                                    v-model="user.address" required maxlength="30" />
                            </div>
                            <div class="form-group ">
                                <label for="phone"><i class="zmdi zmdi-phone material-icons-name"></i></label>
                                <input type="number" name="phone" id="phone" placeholder="Phone Number"
                                    v-model="user.phone" required minlength="11" />
                            </div>
                            <div class="form-group form-button">
                                <input type="submit" name="nextsignup" id="nextsignup" class="form-submit"
                                    value="Next" />
                            </div>
                        </form>
                    </div>
                    <div class="signup-image">
                        <figure><img src="../../assets/images/signup-image.jpg" alt="sing up image"></figure>
                        <a href="/login" class="signup-image-link"><u>I am already member</u>.</a>
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>
<style scoped>
@import url(../../assets/fonts/material-icon/css/material-design-iconic-font.min.css);
@import url(./SignupUser.css);
</style>
<script>
import { ElMessage } from 'element-plus';
export default {
    name: "Register",
    data() {
        return {
            user: {
                id: null,
                username: "",
                email: "",
                password: "",
                firstname: "",
                lastname: "",
                birthday: "",
                phone: "",
                address: "",
                gender: 0
            },
            successful: false,
            message: "",
            isInfo: false,
            repeatpass: ""
        };
    },
    computed: {
        loggedIn() {
            return this.$store.state.auth.status.loggedIn;
        },
    },
    mounted() {
        if (this.loggedIn) {
            this.$router.push("/");
        }
    },
    methods: {
        handleRegister() {
            this.message = "";
            this.successful = false;
            if (this.user.password != this.repeatpass) {
                ElMessage.error("The password does not match the repeat password.");
            }
            else {
                this.$store.dispatch("auth/register", this.user).then(
                    (data) => {
                        this.message = data.message;
                        if (data.status == "OK") {
                            ElMessage({
                                message: data.message,
                                type: 'success',
                            });
                            this.$router.push("/login");
                        }
                        else {
                            ElMessage.error(data.message);
                        }
                    },
                    (error) => {
                        this.message =
                            (error.response &&
                                error.response.data &&
                                error.response.data.message) ||
                            error.message ||
                            error.toString();
                        ElMessage.error(this.message);
                        this.successful = false;
                    }
                );
            }

        },
        checkInfo() {
            this.isInfo = true
        }
    },
};
</script>
