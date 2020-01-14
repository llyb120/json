<template>
    <div id="app">
        <router-view/>
    </div>
</template>

<style lang="less">
    /*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
    /*::-webkit-scrollbar*/
    /*{*/
    /*    width: 5px;  !*滚动条宽度*!*/
    /*    height: 5px;  !*滚动条高度*!*/
    /*}*/

    /*!*定义滚动条轨道 内阴影+圆角*!*/
    /*::-webkit-scrollbar-track*/
    /*{*/
    /*    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);*/
    /*    border-radius: 10px;  !*滚动条的背景区域的圆角*!*/
    /*    !*background-color: lightgrey;!*滚动条的背景颜色*!*!*/
    /*}*/

    /*!*定义滑块 内阴影+圆角*!*/
    /*::-webkit-scrollbar-thumb*/
    /*{*/
    /*    border-radius: 10px;  !*滚动条的圆角*!*/
    /*    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);*/
    /*    !*background-color: grey;  !*滚动条的背景颜色*!*!*/
    /*}*/

    *[tabindex]{
        outline: none;
    }

    html, body {
        /*width: 100%;*/
        /*height: 100%;*/
        margin: 0;
        padding: 0;
        background: #fff;
        /*overflow: hidden;*/
    }

    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        /*text-align: center;*/
        color: #2c3e50;
        box-sizing: border-box;
        /*width: 100%;*/
        /*height: 100%;*/
        /*padding: 5px 5px;*/
    }

    .h100{
        height: 100vh;
        /*height: 100%;*/
        overflow: hidden;
    }
    .ovh{
        overflow: hidden;
    }
    .ovx{
        overflow-x: auto !important;
    }
    .ovy{
        overflow-y: auto !important;
    }
    .cup{
        cursor: pointer;
    }

    .pgh{
        padding-top: 10px;padding-bottom: 10px; border-bottom: 1px solid #eee; margin-bottom: 10px;
    }

    /*.el-scrollbar__wrap{*/
    /*    overflow-x: hidden !important;*/
    /*}*/

    #nav {
        padding: 30px;

        a {
            font-weight: bold;
            color: #2c3e50;

            &.router-link-exact-active {
                color: #42b983;
            }
        }
    }

    .primary-color{
        color:#409EFF;
    }
    .danger-color{
        color: #F56C6C !important;
    }
</style>
