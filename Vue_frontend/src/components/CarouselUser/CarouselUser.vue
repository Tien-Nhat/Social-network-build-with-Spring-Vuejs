<template>
    <div class="multiple-card-slider offset-md-4" style="padding-top: 70px;">
        <div id="carouselExampleControls" class="carousel" data-bs-ride="false" style="width: 630px">
            <div class="carousel-inner">
                <div v-for="(item, index) in listUser" :key="item" class="carousel-item"
                    :style="(index == 0) ? 'margin-left:20px' : ''">
                    <div class="card">
                        <div class="card-body">
                            <a :href="'/profile/' + item.userId"><img
                                    :src="(item.avatar == null) ? unknowAvatar : 'data:image/*;base64,' + item.avatar"
                                    alt="" width="60" height="60" class="rounded-circle"
                                    style="display: block; margin-left: auto; margin-right: auto;" /></a>
                            <a :href="'/profile/' + item.userId" class="text-decoration-none">
                                <p class="center-text" style="text-align: center;"><b style="color: blueviolet;">{{
                    item.firstName }} {{ item.lastName }}</b></p>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls"
                data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls"
                data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Next</span>
            </button>
        </div>
    </div>
</template>

<script>
import "https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/js/bootstrap.bundle.min.js";
import "https://code.jquery.com/jquery-1.10.2.min.js";
export default {
    props: {
        listUser: {
            type: Array,
        }
    },
    data() {
        return {
            unknowAvatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-vector-illustration_268834-541.jpg?size=338&ext=jpg&ga=GA1.1.1700460183.1710028800&semt=ais"
        }
    },
    mounted() {
        setTimeout(() => {
            $('.multiple-card-slider .carousel').each(function () {
                var currentCarouselId = '#' + $(this).attr('id');
                const multipleItemCarousel = document.querySelector(currentCarouselId);

                if (window.matchMedia("(min-width:576px)").matches) {
                    const carousel = new bootstrap.Carousel(multipleItemCarousel, {
                        interval: false,
                        wrap: false
                    })
                    var carouselWidth = $(currentCarouselId + ' .carousel-inner')[0].scrollWidth;
                    var cardWidth = $(currentCarouselId + ' .carousel-item').width();
                    var scrollPosition = 0;
                    $(currentCarouselId + ' .carousel-control-next').on('click', function () {
                        if (scrollPosition < (carouselWidth - (cardWidth * 4))) {
                            scrollPosition = scrollPosition + cardWidth;
                            $(currentCarouselId + ' .carousel-inner').animate({ scrollLeft: scrollPosition }, 600);
                        }
                    });
                    $(currentCarouselId + ' .carousel-control-prev').on('click', function () {
                        if (scrollPosition > 0) {
                            scrollPosition = scrollPosition - cardWidth;
                            $(currentCarouselId + ' .carousel-inner').animate({ scrollLeft: scrollPosition }, 600);
                        }
                    });
                } else {
                    $(multipleItemCarousel).addClass('slide');
                }
            });
        }, 250);
    }
}
</script>
<style scoped>
@import url(./CarouselUser.css);
@import url(https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css);
</style>