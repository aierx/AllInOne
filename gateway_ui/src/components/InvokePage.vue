<template>
    <div>
        <div class="custom-head">
            <div>
                <div style="text-align: start; margin: 20px 0">
                    方法名称： {{ data.methodName==undefined?"未选中":data.methodName}}
                </div>
                <div style="text-align: start; margin: 20px 0">参数：</div>
            </div>
            <div style="display: flex; flex-direction: column; justify-content: space-evenly">
                <div><el-radio v-model="radio" label="1">网关请求</el-radio></div>
                <div><el-radio v-model="radio" label="2">直接请求</el-radio></div>
            </div>
        </div>
        <div v-for="(el, index) in data.params" v-bind:key="index">
            <el-input placeholder="请输入内容" v-model="paramValues[index]" style="margin-top: 20px">
                <template slot="prepend"> [query] {{ el }}:</template>
            </el-input>
        </div>
        <div v-for="(el, index) in data.pathParams" v-bind:key="index">
            <el-input placeholder="请输入内容" v-model="paramValues[index]" style="margin-top: 20px">
                <template slot="prepend">[path] {{ el }}:</template>
            </el-input>
        </div>
        <div v-if="data.requestBody" style="">
            <el-input type="textarea" :rows="7" placeholder="请输入内容" v-model="body" 
            @keydown.tab.native="tabInput($event)">
            </el-input>
        </div>
        <el-input type="textarea" :rows="7" placeholder="请输入内容" style="margin-top: 20px" v-model="result"
            @keydown.tab.native="tabInput($event)">
        </el-input>
        <el-button type="primary" size="default" style="margin-top: 20px" @click="invokeApi">发起请求</el-button>
    </div>
</template>

<script>
export default {
    name: "InvokePage",
    data() {
        return {
            body: "",
            paramValues: [],
            radio: "1",
            result: "",
        };
    },
    props: {
        data: Object,
    },
    /* eslint-disable */
    methods: {
        async invokeApi() {
            let re = [];
            re.push(this.body);
            let tmp = this.data.requestBody ? re : this.paramValues;
            this.data.paramValues = tmp;
            this.data.url = this.data.url.split(";")[0]
            let directUrl = this.data.url
            if (this.radio == "1") {
                // 网关请求
                this.postData("http://127.0.0.1:8080/api", this.data).then((result) => {
                    this.result = JSON.stringify(result, null, "  ");
                });
            } else {
                // 直接请求
                if (!this.data.requestBody) {
                    // 路径参数
                    for (const param in this.data.pathParams) {
                        directUrl = directUrl.replace(/\{[\s\S]*?\}/, this.paramValues[param]);
                    }
                    // query参数
                    let queryParam = ""
                    for (let i = 0; i < this.data.params.length; i++) {
                        queryParam = queryParam + this.data.params[i] + "=" + this.paramValues[i] + "&"

                    }
                    console.log(queryParam)
                    if (this.data.params.length > 0) {
                        directUrl = (directUrl + "?" + queryParam).substring(0, (directUrl + "?" + queryParam).length - 1)
                    }
                    let url1 = "http://" + this.data.host + directUrl;
                    console.log(url1);
                    fetch(url1)
                        .then((res) => res.json())
                        .then((res) => (this.result = JSON.stringify(res, null, "  ")));
                } else {
                    let url1 = "http://" + this.data.host + directUrl
                    this.postData(url1, this.body, false).then((result) => {
                        this.result = JSON.stringify(result, null, "  ");
                    });
                }
            }
        },
        tabInput(e) {
            e.preventDefault();
            const insertText = "  ";
            const elInput = e.target;
            const startPos = elInput.selectionStart;
            const endPos = elInput.selectionEnd;
            if (startPos === undefined || endPos === undefined) return;
            const txt = elInput.value;
            elInput.value = txt.substring(0, startPos) + insertText + txt.substring(endPos);
            elInput.focus();
            elInput.selectionStart = startPos + insertText.length;
            elInput.selectionEnd = startPos + insertText.length;
            this.inputValue = elInput.value;
        },
        async postData(url = "", data = {}, isStr = true) {
            // Default options are marked with *
            const response = await fetch(url, {
                method: "POST", // *GET, POST, PUT, DELETE, etc.
                mode: "cors", // no-cors, *cors, same-origin
                cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
                credentials: "same-origin", // include, *same-origin, omit
                headers: {
                    "Content-Type": "application/json",
                    // 'Content-Type': 'application/x-www-form-urlencoded',
                },
                redirect: "follow", // manual, *follow, error
                referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
                body: isStr ? JSON.stringify(data) : data, // body data type must match "Content-Type" header
            });
            return response.json(); // parses JSON response into native JavaScript objects
        },
    },
    created() { },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.custom-head {
    display: flex;
    justify-content: space-between;
}
</style>
