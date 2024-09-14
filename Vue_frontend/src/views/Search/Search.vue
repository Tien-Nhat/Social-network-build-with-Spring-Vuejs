<template>
    <CarouselUser v-if="searchPostUser.listUser.length != 0" :listUser="searchPostUser.listUser" />
    <div class="container profile">
        <div class="colum">
            <ListPost :posts="searchPostUser.listPost" :loading="loading" />
        </div>
    </div>

</template>

<script>
import Navbar from '@/components/Navbar/Navbar.vue';
import "https://code.jquery.com/jquery-1.10.2.min.js";
import "https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js";
import postService from '@/services/post.service';
import CarouselUser from '@/components/CarouselUser/CarouselUser.vue';
import ListPost from '@/components/List_Post/ListPost.vue';

export default {
    name: 'TimeLine',
    components: {
        Navbar,
        CarouselUser,
        ListPost
    },
    props: {
        txtSearch: {
            type: String,
        },
    },
    data() {
        return {
            response: {
                status: "",
                message: "",
                result: null
            },
            user: {
                avatar: "",
                firstName: "",
                lastName: ""
            },
            searchPostUser: {
                listPost: [],
                listUser: []
            },
            media: [],
            loading: true
        };
    },
    mounted() {
        this.search();
    },
    methods: {
        async search() {
            await postService.searchPostUser(this.$route.query.txtSearch).then(
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
                this.loading = false;
                this.searchPostUser = this.response.result;
            }

        },

    }
}
</script>

<style scoped>
@import url(./Search.css);
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css);
@import url(https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css);
</style>