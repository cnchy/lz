var app = new Vue({
    el: '#app',
    data: {
        users: [{
            id: '',
            userNumber: '',
            name: '',
            createTime: '',
            lstEditTime: '',
            enableStatus: ''
        }],
        addEditor: {
            id: '',
            userNumber: '',
            userPasswd: '',
            name: ''
        },
        editor: {
            id: '',
            avatar: '',
            name: '',                                //教工姓名
            gender: '',                              //性别
            dateOfBirth: '',                         //出生年月
            nation: '',                              //民族
            nativePlace: '',                         //籍贯
            placeOfBirth: '',                        //出生地
            dianhua: '',                             //电话
            dizhi:'',                                //地址
            shenfenzheng:'',                         //身份证
            dateOfJoinParty: '',                     //入党时间
            dateOfJoinWork: '',                      //参加工作时间
            physicalCondition: '',                   //健康情况
            technicalPosition: '',                   //专业技术职务
            familiarMajorAndSpecialty: '',           //熟悉专业有何专长
            fullTimeDegree: '',                      //全日制学历学位
            fullTimeGraduatedUniversityAndMajor: '', //全日制毕业院校系及专业
            partTimeDegree: '',                      //在职学历学位
            partTimeGraduatedUniversityAndMajor: '', //在职毕业院校系及专业
            currentPosition: '',                     //现任职务
            resume: '',                              //简历
            rewardsAndPunishment: '',                //奖惩情况
            annualAssessmentResults: '',             //年度考核结果
            enableStatus: '',
        },
        passwdEditor: {
            id: '',
            userPasswd: '',
            repasswd: '',
        },
        families: [{
            id: '',
            appellation: '',
            userFamilyName: '',
            age: '',
            politicsStatus: '',
            workUnitAndPosition: '',
            createTime: '',
            lastEditTime: '',
            enableStatus: ''
        }],
        addUserFamilyEditor: {
            appellation: '',
            userFamilyName: '',
            age: '',
            politicsStatus: '',
            workUnitAndPosition: '',
        },
        userFamilyEditor: {
            id: '',
            appellation: '',
            userFamilyName: '',
            age: '',
            politicsStatus: '',
            workUnitAndPosition: '',
        },
        pageConf: {
            //设置一些初始值(会被覆盖)
            pageCode: 1, //当前页
            pageSize: 6, //每页显示的记录数
            totalPage: 12, //总记录数
            pageOption: [6, 10, 20], //分页选项
        },

        localUpload: api.user.localUpload,
        currentUserId: '',
        defaultActive: '3',
        //条件查询单独封装的对象
        searchEntity: {},
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
        }
        window.onresize = function() {
            app.changeDiv();
        }
        document.getElementById("header-admin").innerHTML = window.localStorage.getItem("adminNumber") + ",你好";
        this.search(this.pageConf.pageCode, this.pageConf.pageSize);
    },
    mounted() {
        this.$refs.loader.style.display = 'none';
    },
    methods: {
        logout() {
            window.location.href = api.common.logout //更改了密码，注销当前登录状态，重新登录
        },
        _notify(message, type) {
            this.$message({
                message: message,
                type: type
            })
        },
        //刷新列表
        reloadList() {
            this.search(this.pageConf.pageCode, this.pageConf.pageSize);
        },
        //条件查询
        search(pageCode, pageSize) {
            this.$http.post(api.user.findByPage(pageSize, pageCode), this.searchEntity).then(result => {
                this.users = result.body.data.rows;
                this.pageConf.totalPage = result.body.data.total;
            });
        },

        //pageSize改变时触发的函数
        handleSizeChange(val) {
            this.search(this.pageConf.pageCode, val);
            this.pageConf.pageSize = val;
        },
        //当前页改变时触发的函数
        handleCurrentChange(val) {
            this.pageConf.pageCode = val; //为了保证刷新列表后页面还是在当前页，而不是跳转到第一页
            this.search(val, this.pageConf.pageSize);
        },

        //删除按钮
        handleDelete(id) {
            var ids = new Array();
            ids.push(id);
            this.sureDelete(ids);
        },
        //删除
        sureDelete(ids) {
            this.$confirm('你确定永久删除此用户信息？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true
            }).then(() => {
                this.$http.post(api.user.delete, JSON.stringify(ids)).then(result => {
                    if (result.body.code == 200) {
                        this._notify(result.body.msg, 'success')
                        if ((this.pageConf.totalPage - 1) / this.pageConf.pageSize === (this.pageConf.pageCode - 1)) {
                            this.pageConf.pageCode = this.pageConf.pageCode - 1;
                        }
                        this.reloadList();
                    } else {
                        this._notify(result.body.msg, 'error')
                        this.reloadList();
                    }
                });
            }).catch(() => {
                this._notify('已取消删除', 'info')
            });
        },

        //获取用户家庭成员
        getUserFamily() {
            this.$http.get(api.userFamily.findByUserId(this.currentUserId)).then(result => {
                this.families = result.body.data;
            });
        },

        //删除用户家庭成员
        handleUserFamilyDelete(id) {
            var ids = new Array();
            ids.push(id);
            this.$confirm('你确定永久删除此用户信息？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                center: true
            }).then(() => {
                this.$http.post(api.userFamily.delete, JSON.stringify(ids)).then(result => {
                    if (result.body.code == 200) {
                this._notify(result.body.msg, 'success')
                } else {
                    this._notify(result.body.msg, 'error')
                }
                this.getUserFamily();
            });
            }).catch(() => {
                    this._notify('已取消删除', 'info')
                });
        },

        addUserFamily() {
            if (this.addUserFamilyEditor.appellation == null || this.addUserFamilyEditor.appellation == '' || this.addUserFamilyEditor.userFamilyName == null || this.addUserFamilyEditor.userFamilyName == '') {
                this._notify('称谓、姓名不能为空', 'warning')
                return;
            }
            this.addUserFamilyEditor.userId = this.currentUserId;
            this.$http.post(api.userFamily.save, JSON.stringify(this.addUserFamilyEditor)).then(result => {
                this.reloadList();
                if (result.body.code == 200) {
                    this.addUserFamilyEditor = {};
                    this._notify(result.body.msg, 'success');
                    this.getUserFamily();
                } else {
                    this._notify(result.body.msg, 'error')
                }
            });
            this.addUserFamilyEditor = {};
        },
        //触发编辑用户家庭成员按钮
        handleUserFamily() {
            this.userFamilyDialog = true;
            this.families = {}; //清空表单
            this.getUserFamily();
        },
        //关闭编辑窗口
        handleUserFamilyClose(key, keyPath) {
            this.userFamilyDialog = false;
        },
        handleUserFamilyEdit(id) {
            this.userFamilyEditDialog = true;
            this.$http.get(api.userFamily.findById(id)).then(result => {
                this.userFamilyEditor = result.body.data;
        });
        },
        //关闭编辑窗口
        handleUserFamilyEditClose(key, keyPath) {
            this.userFamilyEditDialog = false;
        },
        userFamilyEdit() {
            this.userFamilyEditDialog = false;
            //查询当前id对应的数据
            this.$http.post(api.userFamily.update, JSON.stringify(this.userFamilyEditor)).then(result => {
                if (result.body.code == 200) {
                    this._notify(result.body.msg, 'success')
                } else {
                    this._notify(result.body.msg, 'error')
                }
                this.getUserFamily();
            });
            this.userFamilyEditor = {};
        },

        //触发添加管理员按钮
        handleAdd(id) {
            this.addDialog = true;
        },
        //关闭窗口
        handleAddClose(key, keyPath) {
            this.addDialog = false;
        },
        add() {
            if (this.addEditor.userNumber == null || this.addEditor.userNumber == '' || this.addEditor.userPasswd == null || this.addEditor.userPasswd == '' || this.addEditor.name == null || this.addEditor.name == '') {
                this._notify('输入的信息不能为空', 'warning')
                return;
            }
            var pattern = /^\d{6}$/;
            if (!pattern.test(this.addEditor.userNumber)) {
                this._notify('账号为6位数字', 'warning');
                return;
            }
            if (this.addEditor.userPasswd.length < 5) {
                this._notify('请重新输入密码，密码长度在5位及以上', 'warning');
            } else {
                this.$http.post(api.user.save, JSON.stringify(this.addEditor)).then(result => {
                    this.reloadList();
                    if (result.body.code == 200) {
                        this.addEditor = {};
                        this._notify(result.body.msg, 'success')
                    } else {
                        this._notify(result.body.msg, 'error')
                    }
                });
                this.editor = {};
                this.addDialog = false;
            }
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
            if (this.passwdEditor.userNumber == null || this.passwdEditor.userNumber == '' || this.passwdEditor.userPasswd == null || this.passwdEditor.userPasswd == '') {
                this._notify('输入的信息不能为空', 'warning')
                return;
            }
            var pattern = /^\d{6}$/;
            if (!pattern.test(this.passwdEditor.userNumber)) {
                this._notify('账号为6位数字', 'warning');
                return;
            }
            if (this.passwdEditor.userPasswd.length < 5) {
                this._notify('请重新输入密码，密码长度在5位及以上', 'warning');
            } else{
                this.changePasswdDialog = false;
                this.$http.post(api.user.update, JSON.stringify(this.passwdEditor)).then(result => {
                    if (result.body.code == 200) {
                    this._notify(result.body.msg, 'success');
                    } else {
                        this._notify(result.body.msg, 'error');
                    }
                    this.passwdEditor = {}
                });
            }
        },

        //导出word
        downloadWord() {
            window.open(api.userInfo.downloadWord(this.currentUserId),"_blank");
        },
        //导出wordResult
        downloadWordResult() {
            window.open(api.userInfo.downloadWordResult(this.currentUserId),"_blank");
        },
        //触发编辑用户信息按钮
        handleEdit(userId) {
            this.editDialog = true;
            this.editor = {}; //清空表单
            //查询当前id对应的数据
            this.$http.get(api.userInfo.findByUserId(userId)).then(result => {
                this.editor = result.body.data;
            });
            this.currentUserId = userId;
        },
        //关闭编辑用户信息窗口
        handleEditClose(key, keyPath) {
            this.clearFile()
            this.editDialog = false;
        },
        //修改用户信息
        edit() {
            var data = new FormData();
            if(document.getElementById('avatarInput').value != ''){
                let files = this.$refs.avatarInput.files
                var image = {}
                if(files instanceof Array) {
                    image = files[0]
                } else {
                    image = this.file
                }
                data.append('image', image);
            }
            data.append('id', this.editor.id);
            if(this.editor.name != '') data.append('name', this.editor.name);
            if(this.editor.gender != '') data.append('gender', this.editor.gender);
            if(this.editor.dateOfBirth != '') data.append('dateOfBirth', this.editor.dateOfBirth);
            if(this.editor.dianhua != '') data.append('dianhua', this.editor.dianhua);
            if(this.editor.dizhi != '') data.append('dizhi', this.editor.dizhi);
            if(this.editor.shenfenzheng != '') data.append('shenfenzheng', this.editor.shenfenzheng);
            if(this.editor.nation != '') data.append('nation', this.editor.nation);
            if(this.editor.nativePlace != '') data.append('nativePlace', this.editor.nativePlace);
            if(this.editor.placeOfBirth != '') data.append('placeOfBirth', this.editor.placeOfBirth);
            if(this.editor.dateOfJoinParty != '') data.append('dateOfJoinParty', this.editor.dateOfJoinParty);
            if(this.editor.dateOfJoinWork != '') data.append('dateOfJoinWork', this.editor.dateOfJoinWork);
            if(this.editor.physicalCondition != '') data.append('physicalCondition', this.editor.physicalCondition);
            if(this.editor.technicalPosition != '') data.append('technicalPosition', this.editor.technicalPosition);
            if(this.editor.familiarMajorAndSpecialty != '') data.append('familiarMajorAndSpecialty', this.editor.familiarMajorAndSpecialty);
            if(this.editor.fullTimeDegree != '') data.append('fullTimeDegree', this.editor.fullTimeDegree);
            if(this.editor.fullTimeGraduatedUniversityAndMajor != '') data.append('fullTimeGraduatedUniversityAndMajor', this.editor.fullTimeGraduatedUniversityAndMajor);
            if(this.editor.partTimeDegree != '') data.append('partTimeDegree', this.editor.partTimeDegree);
            if(this.editor.partTimeGraduatedUniversityAndMajor != '') data.append('partTimeGraduatedUniversityAndMajor', this.editor.partTimeGraduatedUniversityAndMajor);
            if(this.editor.currentPosition != '') data.append('currentPosition', this.editor.currentPosition);
            if(this.editor.resume != '') data.append('resume', this.editor.resume);
            if(this.editor.rewardsAndPunishment != '') data.append('rewardsAndPunishment', this.editor.rewardsAndPunishment);
            if(this.editor.annualAssessmentResults != '') data.append('annualAssessmentResults', this.editor.annualAssessmentResults);
            //查询当前id对应的数据
            this.$http.post(api.userInfo.update, data).then(result => {
                this.reloadList();
                if (result.body.code == 200) {
                    this._notify(result.body.msg, 'success')
                    this.clearFile()
                } else {
                    this._notify(result.body.msg, 'error')
                }
            });
            this.editor = {};
            this.editDialog = false;
        },

        clearFile(){
            var file = document.getElementById('avatarInput');
            file.value = ''; //虽然file的value值不能设为有内容的字符，但是可以设置为空字符
            //file.outerHTML = file.outerHTML;
        },

        handleFileChange(e){
            let file = e.target.files[0];
            if(file) {
                this.file = file
                console.log(this.file)
                let reader = new FileReader()
                let that = this
                reader.readAsDataURL(file)
                reader.onload = function(e){
                    // 这里的this 指向reader
                    that.editor.avatar = this.result
                }
            }
        },

        changeAvatar(url){
            this.user.avatar = url;
            var data = {
                id: this.user.id,
                avatar: this.user.avatar
            };
            this.$http.post(api.user.update, JSON.stringify(data)).then(response => {
                this.avatarDialog = false;
                if (response.body.code == 200) {
                    this._notify('更换头像成功', 'success')
                } else {
                    this._notify(response.body.msg, 'error')
                }
            })
        },
        /**
         * 图片上传
         * @param res
         * @param file
         * @param fileList
         */
        //文件上传成功的钩子函数
        handleAvatarSuccess(res, file, fileList) {
            this._notify('图片上传成功', 'success');
            if (res.code == 200) {
                this.user.avatar = res.data.url;
            }
        },
        //文件上传前的前的钩子函数
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isGIF = file.type === 'image/gif';
            const isPNG = file.type === 'image/png';
            const isBMP = file.type === 'image/bmp';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG && !isGIF && !isPNG && !isBMP) {
                this.$message.error('上传图片必须是JPG/GIF/PNG/BMP 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传图片大小不能超过 2MB!');
            }
            return (isJPG || isBMP || isGIF || isPNG) && isLt2M;
        },
        //日期显示格式化
        dateFormat:function(row, column) {
            var date = row[column.property];
            if (date == undefined) {
                return "";
            }
            return moment(date).format("YYYY-MM-DD HH:mm:ss");
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
        }
    },
});
