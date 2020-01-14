<template>
    <div ref="code">
        <pre>
            <code v-html="html" :class="[language]">
            </code>
        </pre>
    </div>
</template>

<script>
    import * as hljs from "highlight.js";

    export default {
        props:{
            language: "Java",
            code: ""
        },
        data(){
            return{
                html:""
            }
        },
        name: "Code",
        mounted() {
            if(this.code){
                import(`../code/${this.code}`).then((r) => {
                    this.html = r.default;
                    this.$nextTick(_ => {
                        let highlight = this.$refs.code.querySelectorAll('pre code');
                        highlight.forEach((block)=>{
                            hljs.highlightBlock(block)
                            // var val = hljs.highlightAuto(block.innerHTML, ["Java"]);
                            // console.log(val)
                            // block.innerHTML = val.value;
                            // block.style.background = "#f8f8f8"
                        })
                    })
                });
            }
        }
    }
</script>

<style scoped>

</style>