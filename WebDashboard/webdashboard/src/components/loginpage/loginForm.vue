<template>
    <div>
        <div class="row justify-content-center">
        <div class="col-sm-4 col-sm-offset-3">
        <label class="sr-only" for="adminid">Admin ID</label>
        <input v-bind:class="textinput" type="text" id="adminid" v-model="newProjectID" title="managerid" placeholder="관리자 계정명"><br>
        <label class="sr-only" for="adminpwd">Admin Password</label>
        <input v-bind:class="textinput" type="password" id="adminpwd" v-model="newProjectPass" title="managerpass" placeholder="관리자 계정 비밀번호"><br>
        <button v-bind:class="primebtn" @click="loginProject">프로젝트 로그인</button> <!--얘가 여기 말고 다른 곳에 있을 경우, props / emit으로 데이터를 주고받아야함 --> 
        </div>
    </div>
    </div>
</template>

<script>
import axios from 'axios'
export default{
    data(){
        return{
            newProjectID:'',
            newProjectPass:'',
            primebtn:'btn btn-primary disabled btn-block',
            textinput:'form-control'
        }
    },
    // props:{
    //     titleprops:{
    //         default:''
    //     }
    // },
    methods:{
        loginProject(){
            var vm = this;
            console.log(this.newProjectID + ' ' + this.newProjectPass);
            // 이 부분에, 암호화를 거쳐서, MySQL에다가 저장해야한다.
            console.log("Login Project")
            // Redirect 테스트
            axios.post('http://49.50.174.246:7676/project/admin/signin', {adminid: vm.newProjectID, adminpwd:vm.newProjectPass})
                    .then(res=>{
                        if(res.data.status == true)
                        {
                            console.log("Success");
                            //this.$router.push(this.$route.query.redirect || '/main'); //-> Redirect 하는 부분
                            this.$router.push('/main');
                            // console.log('Params = ' + vm.titleprops)
                            //this.$router.push({name:'main',params:{projectname:vm.titleprops}})
                        }
                        else{
                            alert('로그인 오류입니다. 아이디와 비밀번호를 확인하세요.')
                        }
            })
            
        }
    }
}
</script>

<style>
    input{
        /* margin:0em 0em 0.5em 0em; */
        display:flex;
        align-items: center;
    }
</style>