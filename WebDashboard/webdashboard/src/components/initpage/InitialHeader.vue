<template>
    <header>
        <!-- <img v-bind:class="mainimg" src="../../../static/img/IoT_main.jpg"> -->
        <h1>KubIoT 초기 설정</h1>
    </header>
</template>

<script>
export default{
    data(){
        return {
        mainimg:'mimg' // 싱글파일 컴포넌트에서는 이렇게 바뀌었음.
        }
    }
}
</script>

<style scoped>
header{
        height: 30%;
    }
    h1{
        color:#2F3B52;
        /* font-weight: 900; */
        margin:2.5rem 0 1.5rem;
        padding-top: 20%;
    }
    .mimg{
        width:300px;
        height: 200px;
        margin-top: 5rem;
    }
</style>