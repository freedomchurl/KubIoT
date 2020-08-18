<template>
    <div>
        <!-- {{this.$router.projectname}} -->
        <div id="flexcontain">
            <MainSide v-bind:id="ID" ref="select"></MainSide>
        <div id='flexmain'>
        <!-- <MainSide v-on:menuselect="clickMenu" ref="select"></MainSide>
        <MainContent v-bind:propsmenuindex="menuindex"></MainContent>-->
        
        <!-- <MainHeader></MainHeader> -->
        <MainContent></MainContent>
        </div>
        </div>
    </div>
</template>


<script>
// import MainHeaderVue from './MainHeader.vue'
import MainSideVue from './MainSidebar.vue'
import MainContentVue from './MainContent.vue'




export default {
    components:{
        // 'MainHeader':MainHeaderVue,
        'MainSide':MainSideVue,
        'MainContent':MainContentVue
    },
    props:['id'],
    created(){
        console.log("Call List");
        // console.log(this.id)
        this.$router.push({path:'/main/list',params:{id:this.$route.params.id}}).catch(()=>{});
    },
    data(){
        return{
            menuindex: -1,
            ID:this.id,
        }
    },
    methods:{
        clickMenu(input){
            this.menuindex = input;
            //this.$refs.select.routesindex(input) // 자식 컴포넌트의 함수를 호출
            //eventbus.$emit('jimin',input);
        },

    }
}
</script>



<style>
/* div#flexmain{
    display:flex;
    flex-direction: row;
} */
html,body{
    height: 100%;
}

</style>