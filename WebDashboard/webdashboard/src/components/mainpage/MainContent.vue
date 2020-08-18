<template>
    <div>
        <!-- <router-view :key="$route.fullPath"></router-view> -->
        <router-view id="contentframe" :key="$route.fullPath"></router-view>
    </div>
</template>


<script>
import {EventBus} from '../../utils/event-bus.js'
//import {EventBus2} from '../../utils/event-bus.js'
import router from '../../router/index.js'
// const routespath = ['/list','/analytic','/group','/admin'];
const routesname = ['list','analytic','group','admin'];

export default {
    data(){
        return{
            currentindex:-1,
            menuname:''
        }
    },
    methods:{
    },
    created(){
        console.log('Test here');
        var vm = this;
        EventBus.$on("click-sidemenu",function(index,name,ID){
            console.log("Event!!" + name);
            //this.currentindex = index;
            vm.menuname = name;
            console.log("ID를 받았다 : " + ID);
            //this.setName(name);
            //router.push('/main' + routespath[index]);
            router.push({name:routesname[index],params:{admin_id:ID}}).catch(()=>{});
            // Redirect 가 안됨.
            //this.selectContent(index);
        });
    },
    props:{
        propsmenuindex:{}
    },
    watch: { 
        $route(to, from) { 
            console.log(to.path + ' ' + from.path);
            if (to.path != from.path) { 
                /* router path가 변경될 때마다 서버로 접근로그를 저장한다. */ 
                //axios.post("/lc/access-log"); 
            } 
            if(to.path=='/main/list')
            {
                console.log("EMldjkfl");
                EventBus.$emit("update-list"); // index를 넘겨준다.
            }
        } 
    }
    // watch:{
    //     propsmenuindex(){
    //         //this.currentindex = this.propsmenuindex;
    //         this.$router.push('/main' + routespath[this.propsmenuindex]);
    //         console.log('1111');
    //         //this.currentindex = -1;''
    //     }
    // },
    // methods:{
    //     setName(name){
    //         this.menuname = name;
    //         //this.$router.push(this.$route.query.redirect ||'/main' + routespath[index]);
    //     }
    // }

}
</script>


<style scoped>
    div{
        /* background-color: yellow; */
        /* float:none; */
        /* width: 100%; */
        overflow:auto;
        /* display:flex; */
    }
    /* #menuname{
        float:inline-start; */
        /* display:flex;
        font-weight: bold;
        font-size:1.8rem; */
        /* width: inherit;
        height:inherit;
        text-align: left;
        padding:1rem 2rem 0rem 3rem;
    } */
    
    /* #contentframe{
        padding: 1.5rem 2rem 2rem 2rem;
    } */
    
</style>