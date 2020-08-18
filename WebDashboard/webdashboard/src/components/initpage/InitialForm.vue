<template>
    <div>
        <div class="row justify-content-center">
        <div class="col-sm-4 col-sm-offset-3">
        <label class="sr-only" for="projectname">Project Name</label>
        <input v-bind:class="textinput" type="text" id="projectname" v-model="newProjectName" title="prodjectname" placeholder="프로젝트명"><br>
        <label class="sr-only" for="adminid">Admin ID</label>
        <input v-bind:class="textinput" type="text" id="adminid" v-model="newProjectID" title="managerid" placeholder="관리자 계정명"><br>
        <label class="sr-only" for="adminpwd">Admin Password</label>
        <input v-bind:class="textinput" type="password" id="adminpwd" v-model="newProjectPass" title="managerpass" placeholder="관리자 계정 비밀번호"><br>
        <label class="sr-only" for="adminpwd_confirm">Admin Password Confirm</label>
        <input v-bind:class="textinput" type="password" if="adminpwd_confirm" v-model="newProjectPassAgain" title="managerpassconfirm" placeholder="관리자 계정 비밀번호 확인"><br>
        <button v-bind:class="primebtn" @click="addProject">프로젝트 생성</button> <!--얘가 여기 말고 다른 곳에 있을 경우, props / emit으로 데이터를 주고받아야함 --> 
        </div>
    </div>
    </div>
</template>

<script>
import axios from 'axios'
import IP from '../../../static/IP.json'
export default{
    
    data(){
        return{
            newProjectName:'',
            newProjectID:'',
            newProjectPass:'',
            newProjectPassAgain:'',
            primebtn:'btn btn-primary disabled btn-block',
            textinput:'form-control'
        }
    },
    methods:{
        
        addProject(){
            var vm = this;
            console.log(this.newProjectName + ' ' + this.newProjectID + ' ' + this.newProjectPass + ' ' + this.newProjectPassAgain);
            // 이 부분에, 암호화를 거쳐서, MySQL에다가 저장해야한다.
            console.log(vm.newProjectName);
            axios.post('http://' + IP.IP+ ':7676/project/projectinfo/create', {projectname: vm.newProjectName
            }).then(res => { console.log(res.data)
            //this.dataset = res.data.payload;
                if(res.data.status == true)
                {
                    axios.post('http://' + IP.IP+ ':7676/project/admin/signup',{adminid: vm.newProjectID, adminpwd:vm.newProjectPass})
                    .then(res=>{
                        if(res.data.status == true)
                        {
                            this.$router.push(this.$route.query.redirect || '/main'); //-> Redirect 하는 부분
                        }
                    })
                }
            })
            // Redirect 테스트
            //this.$router.push(this.$route.query.redirect || '/main'); //-> Redirect 하는 부분
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