<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col h-100 d-inline-block w-100 p-0" style="background-color: black;">
        <el-carousel height="auto" trigger="click" :autoplay="false" ref="carousel">
          <el-carousel-item style="height: auto" v-for="item in listMedia" :key="item">
            <div class="profile-img-list-link">
              <div class="profile-img-content" style="height: 100vh;">
                <img v-if="(isFile(item)) ? item.type.includes('image') : item.contentType.startsWith('image')"
                  :src="(!isFile(item)) ? 'data:' + item.contentType + ';base64,' + item.base64String : createURL(item)"
                  style="display: block;" alt="Responsive image">
                <video v-else style="outline: none; border: none; box-shadow: none; display: block;"
                  :src="(!isFile(item)) ? 'data:' + item.contentType + ';base64,' + item.base64String : createURL(item)"
                  controls></video>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>

      </div>
      <div class="col col-lg-2 p-0">
        <div class="card-rounded">
          <div class="card-header">
            <div class="d-flex align-items-center justify-content-between">
              <div class="d-flex align-items-center">
                <a href="#"><img :src="(user.avatar != null) ? 'data:image/*;base64,' + user.avatar : unknowAvatar"
                    alt="" width="50" height="50" class="rounded-circle" /></a>
                <div class="flex-fill ps-2">
                  <div class="fw-bold"><a href="#" class="text-decoration-none">
                      {{ user.firstName + " " + user.lastName }}
                    </a>
                  </div>
                  <div class="small text-body text-opacifty-50">
                    {{ formatDate(user.createdDatetime) }}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-body">

          </div>
          <div class="card-footer">
            <div class="d-flex align-items-center">
              <img :src="(user.avatar != null) ? 'data:image/*;base64,' + user.avatar : unknowAvatar" alt="" width="35"
                class="rounded-circle" />
              <div class="flex-fill ps-2">
                <div class="position-relative d-flex align-items-center" style="margin-right: 10px;">
                  <input type="text" class="form-control rounded-pill bg-white bg-opacity-15"
                    style="padding-right: 120px;" placeholder="Write a comment..." />
                  <div class="position-absolute end-0 text-center">
                    <a @click.prevent="setActiveItem(2)" class="text-body text-opacity-50 me-2"><i
                        class="fa fa-smile"></i></a>
                    <a href="#" class="text-body text-opacity-50 me-2"><i class="fa fa-camera"></i></a>
                    <a href="#" class="text-body text-opacity-50 me-2"><i class="fa fa-video"></i></a>
                    <a href="#" class="text-body text-opacity-50 me-3"><i class="fa fa-paw"></i></a>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>
<script>
export default {
  name: 'Post',
  props: {
    listMedia: {
      type: Array,
    },
    indexMedia: {
      type: Number,
    },
    user: {
      type: Object,
    }
  },
  data() {
    return {
      centerDialogVisible: false,
      response: {
        status: "",
        message: "",
        result: null
      },
      txtSearch: "",
      unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
    }

  },
  methods: {
    setActiveItem() {
      setTimeout(() => {
        this.$refs.carousel.setActiveItem(this.indexMedia);
      }, 0);
    },
    isFile(item) {
      return item instanceof File;
    },
    createURL(itemFile) {
      return URL.createObjectURL(itemFile);
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
  }
}
</script>
<style scoped>
@import url(./DetailMedia.css);
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css);
</style>