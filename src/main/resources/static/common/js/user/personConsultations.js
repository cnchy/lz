var app = new Vue({
    el: '#app',
    data: {
        // 需要后台提供数据
        my_options: [{
            label: '个人述廉材料上传',
            value: '1'
        }],
        value: '',
        isSubmit: false,
        openTime: '',
        attention: '',

        years:[],

        // 该页面的全局变量
        year: new Date().getFullYear().toString(),

    	infos: {
            id: '',
            userId: '',
            year: '',
            path: ''
        },
        userId: '',
        defaultActive: '5',
        mobileStatus: false, //是否是移动端
        sidebarStatus: true, //侧边栏状态，true：打开，false：关闭
        sidebarFlag: ' openSidebar ', //侧边栏标志
    },
    created() {
        window.onload = function () {
            app.changeDiv();
        }
        window.onresize = function () {
            app.changeDiv();
        }
        document.getElementById("header-user").innerHTML = window.localStorage.getItem("userName") + ",你好";
        this.userId = window.localStorage.userId;
        this.getUserInfo(this.userId);
        this.getConsulation();
    },
    mounted() {
        this.$refs.loader.style.display = 'none';
    },
    methods: {
        getConsulation() {
            axios.get('/user/personConsultations/getConsulation').then(response => {
                if (response.data.code !== 200) this._notify(response.data.msg);
                else {
                    if (response.data.data.on) {
                        this.openTime = "临时开放中......";
                    } else {
                        this.openTime = response.data.data.dateStart + "  至  " + response.data.data.dateEnd;
                    }
                    this.attention = response.data.data.attention.replace(/\n/g,"<br/>");
                }

            }).catch(error => (this._notify("获取开放时间， 注意事项信息失败", "error")));
        },

        onSubmit() {
            try {
                let files = document.getElementById('file').files;
                this._checkUploadFile(files, this.value);
                this._uploadFile(files[0], window.localStorage.userId, this.year);
            } catch (e) {
                this._notify(e, "error");
            }
        },

        _checkUploadFile(files, value) {
            if (files.length === 0) throw new Error("请选择文件");
            if (value !== '1') throw new Error("请选择填报类型");
        },

        _uploadFile(file, userId, year) {
            let param = new FormData();
            param.append('file', file);
            param.append('userId', userId);
            param.append('year', year);

            axios.post('personConsultations/upload', param).then(response => {
                console.log(response);
                if (response.data.code !== 200) this._notify(response.data.msg);
                else {
                    this._notify("填报成功", "success");
                    this.getUserInfo(this.userId);
                }
            }).catch(error => {
                this._notify(error, "warning");
            });
        },

        logout() {
            window.location.href = api.common.logout //更改了密码，注销当前登录状态，重新登录
        },
        _notify(message, type) {
            this.$message({
                message: message,
                type: type
            })
        },
        downloadWord() {
        	window.open(api.personConsultations.downloadWord(this.userId, this.year));
        },
        //获取当前用户信息
        getUserInfo(userId) {
            this.$http.get(api.personConsultations.findByUserId(userId)).then(result => {
                this.infos = result.body.data;
            	for(var i=0;i<this.infos.length;i++){
                	var info = this.infos[i];
                	this.years.push(info.year);
                }
                this.isSubmit = this.download();
            });
        },

        //触发修改密码按钮
        handleChangePasswd(id, userNumber) {
            this.changePasswdDialog = true;
            this.passwdEditor = {}; //清空表单
            this.passwdEditor.id = id;
            this.passwdEditor.userNumber = userNumber;
        },
        //关闭窗口
        handleChangePasswdClose(key, keyPath) {
            this.changePasswdDialog = false;
        },
        changePasswd() {
            if (this.pass.userPasswd.length < 5) {
                this._notify('请重新输入密码，密码长度在5位及以上', 'warning');
            } else if (this.pass.userPasswd != this.pass.repassword) {
                this._notify('两次输入的密码不一致', 'warning');
            } else {
                this.pass.id = this.userId;
                this.$http.post(api.user.update, JSON.stringify(this.pass)).then(result => {
                    if (result.body.code == 200) {
                        this._notify(result.body.msg, 'success');
                        this.changePasswdDialog = false;
                        //执行/logout请求
                        //window.location.href = '/user/logout'; //更改了密码，注销当前登录状态，重新登录
                    } else {
                        this._notify(result.body.msg, 'error');
                    }
                    this.clearPass();
                });
            }
        },
        clearPass() {
            this.pass.repassword = '';
            this.pass.userPasswd = '';
        },

        /**
         * 监听窗口改变UI样式（区别PC和Phone）
         */
        changeDiv() {
            let isMobile = this.isMobile();
            if (isMobile) {
                //手机访问
                this.sidebarFlag = ' hideSidebar mobile ';
                this.sidebarStatus = false;
                this.mobileStatus = true;
            } else {
                this.sidebarFlag = ' openSidebar';
                this.sidebarStatus = true;
                this.mobileStatus = false;
            }
        },
        isMobile() {
            let rect = body.getBoundingClientRect();
            return rect.width - RATIO < WIDTH
        },
        handleSidebar() {
            if (this.sidebarStatus) {
                this.sidebarFlag = ' hideSidebar ';
                this.sidebarStatus = false;

            } else {
                this.sidebarFlag = ' openSidebar ';
                this.sidebarStatus = true;
            }
            let isMobile = this.isMobile();
            if (isMobile) {
                this.sidebarFlag += ' mobile ';
                this.mobileStatus = true;
            }
        },
        //蒙版
        drawerClick() {
            this.sidebarStatus = false;
            this.sidebarFlag = ' hideSidebar mobile '
        },
        download() {
            if(this.years.indexOf(this.year) != -1){
            	return true;
            }
            return false;
        }
    },
});
