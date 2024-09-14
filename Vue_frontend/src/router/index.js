import { createRouter, createWebHistory } from 'vue-router';
import SignupUser from '@/views/Signup/SignupUser.vue';
import TimeLine from '@/views/TimeLine/TimeLine.vue';
import store from '@/store';
import Login from '../views/Signin/Login.vue';
import Profile from '@/views/Profile/Profile.vue';
import EditProfile from '@/views/Edit_Profile/EditProfile.vue';
import Search from '@/views/Search/Search.vue';
import Admin from '@/views/Admin/Admin.vue';
import Post from '@/views/Post/Post.vue';
import ChangePassword from '@/views/ChangePassword/ChangePassword.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: TimeLine,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/register',
      name: 'signup',
      component: SignupUser,
    },

    {
      path: '/profile/:id',
      name: 'profile',
      component: Profile,
      meta: { requiresAuth: true },
    },
    {
      path: '/edit',
      name: 'edit',
      component: EditProfile,
    },
    {
      path: '/search',
      name: 'search',
      component: Search,
      props: (route) => ({ txtSearch: route.query.txtSearch }),
    },
    {
      path: '/admin',
      name: 'admin',
      component: Admin,
    },
    {
      path: '/post/:id',
      name: 'post',
      component: Post,
      meta: { requiresAuth: true },
    },
    {
      path: '/change-password',
      name: 'changePass',
      component: ChangePassword,
    },
  ],
});
router.beforeEach((to, from, next) => {
  const loggedIn = store.state.auth.status.loggedIn;
  const currentUser = store.state.auth.user;

  if (to.matched.some((record) => record.meta.requiresAuth) && !loggedIn) {
    next({
      name: 'login',
      query: { redirect: to.fullPath },
    });
  } else if (loggedIn) {
    if (!currentUser['roles'].includes('ROLE_ADMIN')) {
      switch (to.name) {
        case 'admin' || 'register' || 'login':
          next({
            path: '/',
          });
          break;
        default:
          next();
          break;
      }
    } else
      switch (to.name) {
        case 'login' || 'register':
          next({
            path: '/',
          });
          break;
        default:
          next();
          break;
      }
  } else next();
});
export default router;
