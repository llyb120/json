<template>
    <div class="box">
        <div class="left">
            <el-scrollbar style="height: 100%;">
                <template v-for="(v,i) in menus">
                    <h4>{{v[0]}}</h4>
                    <div v-for="vv in v[2]">
                        <el-link :underline="false" @click="go(vv)">{{vv[0]}}</el-link>
                    </div>
                </template>
            </el-scrollbar>
        </div>
        <div class="right">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    import a from "../code/a.js"
    export default {
        name: "Home",
        data(){
            return {
                html:a,
                menus: [
                    ["开发指南", "", [
                        ["快速上手", "common/a"]
                    ]],
                    ["常用方法", "", [

                    ]]
                ]
            }
        },
        methods:{
            go(v){
                console.log(v)
                this.$router.push("/" + v[1]);
            }
        }
    }
</script>

<style scoped lang="less">
    .box{
        width: 980px;
        margin: 0 auto;
        display: flex;

        .left{
            width: 200px;
            height: 100vh;
            overflow-x: hidden;
        }
        .right{
            flex: 1;
        }
    }
</style>