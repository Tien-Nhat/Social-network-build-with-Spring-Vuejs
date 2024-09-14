<template>
	<div class="main">
		<section class="sign-in">
			<div class="container">
				<div class="signin-content">
					<div class="signin-image">
						<figure><img src="../../assets/images/signin-image.jpg" alt="sing up image"></figure>
						<a href="/register" class="signup-image-link"><u>Create an account</u>.</a>
					</div>

					<div class="signin-form">
						<h2 class="form-title">Sign in</h2>
						<form method="POST" @submit.prevent="loginUser" class="register-form" id="login-form">
							<div class="form-group">
								<label for="your_name"><i class="zmdi zmdi-account material-icons-name"></i></label>
								<input v-model="user.username" type="text" name="your_name" id="your_name"
									placeholder="Your Name" required maxlength="30" />
							</div>
							<div class="form-group">
								<label for="your_pass"><i class="zmdi zmdi-lock"></i></label>
								<input v-model="user.password" type="password" name="your_pass" id="your_pass"
									placeholder="Password" required maxlength="30" />
							</div>

							<div class="form-group form-button">
								<input type="submit" name="signin" id="signin" class="form-submit" value="Log in" />
							</div>
						</form>
						<div class="social-login">
							<span class="social-label">Or login with</span>
							<ul class="socials">
								<GoogleLogin :callback="loginGoogle" popup-type="CODE" prompt>
									<li><a href="#"><i class="display-flex-center zmdi zmdi-google"></i></a></li>
								</GoogleLogin>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</section>
	</div>
</template>
<style scoped>
@import url(../../assets/fonts/material-icon/css/material-design-iconic-font.min.css);
@import url(./SigninUser.css);
</style>
<script>
import { ElMessage } from 'element-plus';
import { googleTokenLogin } from "vue3-google-login";
import authService from '@/services/auth.service';
import router from '@/router';
export default {
	name: 'AddUser',
	data() {
		return {
			user: {
				username: "",
				password: ""
			},
			message: "",
		}
	},
	computed: {
		loggedIn() {
			return this.$store.state.auth.status.loggedIn;
		},
		currentUser() {
			return this.$store.state.auth.user;
		},
	},
	created() {
		if (this.loggedIn) {
			if (this.currentUser['roles'].includes('ROLE_ADMIN'))
				this.$router.push("/admin");
			else
				this.$router.push("/");
		}
	},
	methods: {
		async loginUser() {
			console.log(import.meta.env.VITE_API_URL + "/jdd");

			this.$store.dispatch("auth/login", this.user).then(
				(response) => {
					if (this.currentUser['roles'].includes('ROLE_ADMIN'))
						this.$router.push("/admin");
					else
						this.$router.push("/");
					ElMessage({
						message: response.message,
						type: 'success',
					});

				},
				(error) => {
					this.message =
						(error.response &&
							error.response.data &&
							error.response.data.message) ||
						error.message ||
						error.toString();
					ElMessage.error(this.message);
				}
			);
		}, async loginGoogle(response) {
			var oauth = {
				code: "",
				type: ""
			};
			if (response.credential) {
				oauth.code = response.credential,
					oauth.type = "credential"
			}
			else {
				oauth.code = response.code,
					oauth.type = "code"
			}

			this.$store.dispatch("auth/logingg", oauth).then(
				(response) => {
					this.response = response.data;
					this.$router.push("/");
					ElMessage({
						message: response.message,
						type: 'success',
					});
				},
				(error) => {
					this.message =
						(error.response &&
							error.response.data &&
							error.response.data.message) ||
						error.message ||
						error.toString();
					ElMessage.error(this.message);
				}
			);
		}

	}
}
</script>
