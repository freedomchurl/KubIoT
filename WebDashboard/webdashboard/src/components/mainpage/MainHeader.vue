<template>
    <div>
        <form class="form-inline upper-div">
        <span id="title">{{ProjectName}}</span>
        <!-- <div class="row justify-content-center"> -->
        <!-- <div class="col-sm-4 col-sm-offset-3"> -->
            <!-- <label class="sr-only" for="search" style="display:inline">search</label> -->
            
        <input v-bind:class="searching" style="width:50%" type="text" id="search" v-model="searchKey" title="managerid" :placeholder="searchdescription">
        <button v-on:click="searchAll" style="width:10%" v-bind:class="searchButton">검색</button>
        </form>       
        <hr>
    </div>
</template>


<script>
import axios from 'axios'
export default {
    data(){
        return{
            ProjectName:'', // 추후에, DB서버로부터 가져와야한다.,
            searchKey:'',
            searchdescription:'디바이스 정보, 그룹 ID 등 검색',
            searching:'searchinput form-control',
            searchButton:'searchbutton btn btn-primary'
        }
    },
    methods:{
        //method는 둘다 사용할 수 있다. 
        getProjectName:function(){

        },
        getProjectInfo(){

        },
        searchAll(){
            console.log('Search Button Clicked');
        }        
    },
    created(){
        var vm = this;
        console.log('Header Created');
        axios.get('http://49.50.174.246:7676/project/projectinfo/get').then(res => { console.log(res.data)

            vm.ProjectName = res.data.payload.projectname;
       
        })
    },
    computed:{
        searchProcess(){
            //this.searchKey = this.searchKey + '!!'
            
            console.log('Detect');
            return this.searchKey;
        }
    }
}
</script>

<style scoped>
    div{
        background-color:#34314c;
    }
    .searchbutton{
        display:inline-block;
        background-color: #47b8e0;
        margin:0rem 0rem 0rem 0.5rem;
    }
    .searchinput{
        display:inline;
        margin-left:1rem;
    }

    #title{
        color:white;
        width:30%;
        font-size: 3rem;
        text-align: left;
        margin-left:2rem;
        margin-bottom: 0.5rem;
        margin-top:0.2rem;
        
    }
    hr{
        border-top:2px solid;
        margin:0rem 0.5rem 0rem 0.5rem;
    }
    
    .upper-div{
        padding-top :1rem;
        padding-bottom: 1rem;
    }
</style>