<template>
  <div id:app>
    <Navbar ref="navbar" v-if="loggedIn" />
    <router-view :message="message" :statusFriend="statusFriend" :friend="friend"></router-view>
  </div>
</template>
<script>
import Navbar from "./components/Navbar/Navbar.vue";
import Stomp from "webstomp-client";
import SockJS from 'sockjs-client';
import friendService from "./services/friend.service";

export default {
  components: {
    Navbar
  },
  computed: {
    loggedIn() {
      if (this.$store.state.auth.status.loggedIn)
        this.initialUserId = this.$store.state.auth.user.id;
      this.connect(this.$store.state.auth.status.loggedIn);
      return this.$store.state.auth.status.loggedIn;
    },
  },

  data() {
    return {
      message: "",
      statusFriend: {},
      initialUserId: null,
      friend: {},
      ban: false
    }
  },
  methods: {
    connect(loggedIn) {
      if (loggedIn) {
        this.socket = new SockJS(import.meta.env.VITE_API_URL + "/websocket");
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.debug = f => f;
        this.stompClient.connect({ username: this.$store.state.auth.user.username, },
          frame => {

            this.connected = true;
            this.changeStatusActivity(false);

            this.stompClient.subscribe('/users/notifi/messages', tick => {
              this.$refs.navbar.addNotiFi(tick.body)
            });

            this.stompClient.subscribe('/users/friend/messages', tick => {
              this.$refs.navbar.notifiFriend(tick.body);
              this.friend = JSON.parse(tick.body);
            });

            this.stompClient.subscribe('/users/chat/messages', tick => {
              this.message = tick.body
            });

            this.stompClient.subscribe("/update/messages", tick => {
              this.statusFriend = JSON.parse(tick.body);
            });

            this.stompClient.subscribe("/users/ban/messages", tick => {
              this.$refs.navbar.ban(tick.body);
            });

          },
          error => {
            this.changeStatusActivity(true);
            this.connected = false;
          }
        );
      }
      else {
        this.changeStatusActivity(true);
        this.initialUserId = null;
      }
    },
    async changeStatusActivity(isDisconnect) {
      await friendService.changeStatusActivity(isDisconnect, this.initialUserId).then(
        (response) => {
          this.response = response.data;
        },
        (error) => {
          this.response =
            error.response

        }
      );
    }
  }
}
</script>