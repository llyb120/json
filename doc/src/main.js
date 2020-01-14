import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Element from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(Element, {size: 'mini', zIndex: 3000});

// import VueClipboard from 'vue-clipboard2'
// Vue.use(VueClipboard);


Vue.config.productionTip = false

import 'highlight.js/styles/atom-one-dark.css'
// hljs.configure({
//     languages:["Java"],
// })
// Vue.directive('highlight',function (el) {
//     let highlight = el.querySelectorAll('pre code');
//     highlight.forEach((block)=>{
//         hljs.highlightBlock(block)
//         // var val = hljs.highlightAuto(block.innerHTML, ["Java"]);
//         // console.log(val)
//         // block.innerHTML = val.value;
//         // block.style.background = "#f8f8f8"
//     })
// })

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
