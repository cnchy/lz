var app = new Vue({
    el: '#app',
    data: {

        dateStart: '',
        dateEnd: '',
        on: false,
        attention: '',

        currentUserId: '',
        defaultActive: '6',
        editDialog: false,
        addDialog: false,
        changePasswdDialog: false,
        userFamilyDialog: false,
        userFamilyEditDialog: false,
        mobileStatus: false, //是否是移动端
        sidebarStatus: true, //侧边栏状态，true：打开，false：关闭
        sidebarFlag: ' openSidebar ', //侧边栏标
    },
    created() {
        window.onload = function() {
            app.changeDiv();
        };
        window.onresize = function() {
            app.changeDiv();
        };
        document.getElementById("header-admin").innerHTML = window.localStorage.getItem("adminNumber") + ",你好";
        this.getConsulation();
    },
    mounted() {
        this.$refs.loader.style.display = 'none';
    },
    methods: {
        getConsulation() {
            axios.get('/manage/getConsulation').then(response => {
                if (response.data.code !== 200) this._notify(response.data.msg);
                else {
                    this.on = response.data.data.on;
                    this.dateStart = new Date(response.data.data.dateStart);
                    this.dateEnd = new Date(response.data.data.dateEnd);
                    this.attention = response.data.data.attention;
                }

            }).catch(error => (this._notify("获取信息失败", "error")));
        },

        onSubmit() {
            try {
                this._checkSubmitArgvs(this.dateStart, this.dateEnd);
                this._submit(this.dateStart, this.dateEnd, this.attention, this.on, this._submitSuccess, this._submitFail);
            } catch (e) {
                this._notify(e.toString(), 'error');
            }
        },

        _checkSubmitArgvs(dateStart, dateEnd) {
            function checkDataType(data, type, errorInfo='') {
                if (Object.prototype.toString.call(data) !== type) throw new Error(errorInfo);
            }

            // 通过判断类型累判断有没有选择时间
            checkDataType(dateStart, '[object Date]', '请选择开始时间');
            checkDataType(dateEnd, '[object Date]', '请选择结束时间');
        },

        _submit(dateStart, dateEnd, attention, on, successFunc, FailFunc) {
            let formDate = new FormData();
            formDate.append('dateStart', dateStart);
            formDate.append('dateEnd', dateEnd);
            formDate.append('attention', attention);
            formDate.append('on', on);
            axios.post('/manage/setConsulation', formDate).then(response => {
                if (response.data.code !== 200) FailFunc(response);
                else if (successFunc !== undefined) successFunc(response);
            });
        },

        _submitFail(response) {
            this._notify(response.data.msg, 'error');
        },

        _submitSuccess() {
          this._notify('编辑成功', 'success');
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
        //蒙版
        drawerClick() {
            this.sidebarStatus = false;
            this.sidebarFlag = ' hideSidebar mobile '
        }
    },
});
