<template>
    <div>
        <MainHeader></MainHeader>
        <div id='flexmain'>
        <!-- <MainSide v-on:menuselect="clickMenu" ref="select"></MainSide>
        <MainContent v-bind:propsmenuindex="menuindex"></MainContent>-->
        <MainSide ref="select"></MainSide>
        <MainContent></MainContent>
        </div>
    </div>
</template>


<script>
import MainHeaderVue from './MainHeader.vue'
import MainSideVue from './MainSidebar.vue'
import MainContentVue from './MainContent.vue'




export default {
    components:{
        'MainHeader':MainHeaderVue,
        'MainSide':MainSideVue,
        'MainContent':MainContentVue
    },
    beforeMount(){
        console.log("Init");
        this.$router.push('/main/list').catch(()=>{});
    },
    data(){
        return{
            menuindex: -1
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



<style scoped>
/* div#flexmain{
    display:flex;
    flex-direction: row;
} */
</style>