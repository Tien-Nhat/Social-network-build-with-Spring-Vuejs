<template>
    <div class="container profile" style="padding-top: 70px;">
        <div class="colum">
            <ListPost :posts="posts" :loading="loading" :friend="friend"/>
        </div>
    </div> 
</template>

<script>
import "https://code.jquery.com/jquery-1.10.2.min.js";
import "https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js";
import postService from '@/services/post.service';
import ListPost from '@/components/List_Post/ListPost.vue';

export default {
    name: 'TimeLine',
    components: {
        ListPost
    },
    props:{
        txtSearch: {
            type: String,
        },
        friend: {
            type: Object
        }
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
            posts:[], 
            media: [],
            loading: true
        };
    },
    mounted() {
        this.getPost();
    },
    methods: {
        async getPost() {
            await postService.getPost(this.$route.params.id).then(
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
            if (this.response.status == "SUCCESS"){
                this.loading = false;
                this.posts.push(this.response.result);
            }
                
        },
       
    }
}
</script>

<style scoped>
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css);
@import url(https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css);
</style>