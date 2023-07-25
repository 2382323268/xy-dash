"use strict";(self["webpackChunkxy_dash_admin"]=self["webpackChunkxy_dash_admin"]||[]).push([[691,237],{3237:function(e,a,l){l.r(a),l.d(a,{default:function(){return p}});var t=l(3396),u=l(4870),i=l(7412),d=l(7178);const o={class:"dialog-footer"};var s={__name:"dialog",props:{id:{type:Number,default:-1,required:!0},dialogTitle:{type:String,default:"",required:!0},dialogVisible:{type:Boolean,default:!1,required:!0}},emits:["update:modelValue","initUserList"],setup(e,{emit:a}){const l=e,s=(0,u.iH)({id:-1,userName:"",password:"",type:0,port:null,name:"",ip:""}),r=(0,u.iH)({userName:[{required:!0,message:"请输入用户名"}],password:[{required:!0,message:"密码不能为空"}],port:[{required:!0,message:"端口不能为空"}],name:[{required:!0,message:"数据源名称不能为空"}],ip:[{required:!0,message:"主机不能为空"}]}),n=(0,u.iH)(null),p=async e=>{const a=await i.ZP.get("admin_api/v1/dataSources/"+e);s.value=a.data.data},m=async()=>{n.value.validate((async e=>{if(e){const e=await i.ZP.post("admin_api/v1/dataBase/connect",s.value);e.data.success?d.z8.success("连接成功！"):d.z8.error("连接失败！")}else console.log("fail")}))};(0,t.YP)((()=>l.dialogVisible),(()=>{let e=l.id;-1!=e?p(e):s.value={id:-1,userName:"",password:"",type:0,port:null,name:"",ip:""}}));const c=()=>{n.value.resetFields(),a("update:modelValue",!1)},v=()=>{n.value.validate((async e=>{if(e)if(-1==s.value.id){let e=await i.ZP.post("admin_api/v1/dataSources",s.value);w(e)}else{let e=await i.ZP.post("admin_api/v1/dataSources/"+s.value.id,s.value);w(e)}else console.log("fail")}))},w=e=>{let l=e.data.success;l?(d.z8.success("执行成功！"),n.value.resetFields(),a("initUserList"),c()):d.z8.error(e.data.msg)};return(a,l)=>{const u=(0,t.up)("el-input"),i=(0,t.up)("el-form-item"),d=(0,t.up)("el-radio"),p=(0,t.up)("el-radio-group"),w=(0,t.up)("el-form"),f=(0,t.up)("el-button"),g=(0,t.up)("el-dialog");return(0,t.wg)(),(0,t.j4)(g,{modelValue:e.dialogVisible,title:e.dialogTitle,width:"30%","clearValidate:":"",onClose:c},{footer:(0,t.w5)((()=>[(0,t.Wm)(f,{class:"test",plain:"",onClick:m},{default:(0,t.w5)((()=>[(0,t.Uk)("测试连接")])),_:1}),(0,t._)("span",o,[(0,t.Wm)(f,{type:"primary",onClick:v},{default:(0,t.w5)((()=>[(0,t.Uk)("确认")])),_:1}),(0,t.Wm)(f,{onClick:c},{default:(0,t.w5)((()=>[(0,t.Uk)("取消")])),_:1})])])),default:(0,t.w5)((()=>[(0,t.Wm)(w,{ref_key:"formRef",ref:n,model:s.value,rules:r.value,"label-width":"100px"},{default:(0,t.w5)((()=>[(0,t.Wm)(i,{label:"数据源名称",prop:"name"},{default:(0,t.w5)((()=>[(0,t.Wm)(u,{modelValue:s.value.name,"onUpdate:modelValue":l[0]||(l[0]=e=>s.value.name=e)},null,8,["modelValue"])])),_:1}),(0,t.Wm)(i,{label:"主机",prop:"ip"},{default:(0,t.w5)((()=>[(0,t.Wm)(u,{modelValue:s.value.ip,"onUpdate:modelValue":l[1]||(l[1]=e=>s.value.ip=e)},null,8,["modelValue"])])),_:1}),(0,t.Wm)(i,{label:"端口",style:{width:"180px"},prop:"port"},{default:(0,t.w5)((()=>[(0,t.Wm)(u,{modelValue:s.value.port,"onUpdate:modelValue":l[2]||(l[2]=e=>s.value.port=e)},null,8,["modelValue"])])),_:1}),(0,t.Wm)(i,{label:"用户名",style:{width:"300px"},prop:"userName"},{default:(0,t.w5)((()=>[(0,t.Wm)(u,{modelValue:s.value.userName,"onUpdate:modelValue":l[3]||(l[3]=e=>s.value.userName=e)},null,8,["modelValue"])])),_:1}),(0,t.Wm)(i,{label:"密码",style:{width:"300px"},prop:"password"},{default:(0,t.w5)((()=>[(0,t.Wm)(u,{modelValue:s.value.password,"onUpdate:modelValue":l[4]||(l[4]=e=>s.value.password=e),type:"password"},null,8,["modelValue"])])),_:1}),(0,t.Wm)(i,{label:"类型",prop:"type"},{default:(0,t.w5)((()=>[(0,t.Wm)(p,{modelValue:s.value.type,"onUpdate:modelValue":l[5]||(l[5]=e=>s.value.type=e)},{default:(0,t.w5)((()=>[(0,t.Wm)(d,{label:0},{default:(0,t.w5)((()=>[(0,t.Uk)("mysql")])),_:1}),(0,t.Wm)(d,{label:1},{default:(0,t.w5)((()=>[(0,t.Uk)("sqlserver")])),_:1})])),_:1},8,["modelValue"])])),_:1})])),_:1},8,["model","rules"])])),_:1},8,["modelValue","title"])}}},r=l(89);const n=(0,r.Z)(s,[["__scopeId","data-v-1d95a88f"]]);var p=n},2691:function(e,a,l){l.r(a),l.d(a,{default:function(){return g}});var t=l(3396),u=l(4870),i=l(7412),d=l(3237),o=l(7178);const s=e=>((0,t.dD)("data-v-22149f90"),e=e(),(0,t.Cn)(),e),r=["onClick"],n=["onClick"],p=["onClick"],m=s((()=>(0,t._)("span",{style:{"font-size":"larger"}},"您确定要删除这条记录吗？",-1))),c={class:"dialog-footer"};var v={__name:"index",setup(e){const a=(0,u.iH)(0),l=(0,u.iH)(null),s=(0,u.iH)(!1),v=(0,u.iH)(!1),w=(0,u.iH)(""),f=(0,u.iH)(-1),g=(0,u.iH)(!1),_=(0,u.iH)(-1),y=async()=>{const e=await i.ZP.get("admin_api/v1/dataSources",C.value);l.value=e.data.data.records,a.value=e.data.data.total},V=e=>{e?(f.value=e,w.value="数据源修改"):(f.value=-1,w.value="数据源添加"),s.value=!0},W=async e=>{g.value=!0;try{const a=await i.ZP.get("admin_api/v1/dataBase/"+e+"/connect");a.data.success?o.z8.success("连接成功！"):o.z8.error("连接失败！")}finally{g.value=!1}},b=async()=>{await i.ZP.post("admin_api/v1/dataSources/removes?ids="+_.value);v.value=!1,y()},k=e=>{C.value.current=1,C.value.size=e,y()},h=e=>"0"==e.type?"mysql":"1"==e.type?"sqlserver":void 0,U=e=>{C.value.current=e,y()},C=(0,u.iH)({name:"",current:1,size:10});return y(),(e,i)=>{const o=(0,t.up)("el-input"),z=(0,t.up)("el-col"),x=(0,t.up)("el-button"),H=(0,t.up)("el-row"),q=(0,t.up)("el-table-column"),P=(0,t.up)("el-table"),Z=(0,t.up)("el-pagination"),S=(0,t.up)("el-dialog"),N=(0,t.Q2)("loading");return(0,t.wg)(),(0,t.iD)(t.HY,null,[(0,t.Wm)(H,{gutter:20,class:"header"},{default:(0,t.w5)((()=>[(0,t.Wm)(z,{span:4},{default:(0,t.w5)((()=>[(0,t.Wm)(o,{placeholder:"请输入数据源名称...",modelValue:C.value.name,"onUpdate:modelValue":i[0]||(i[0]=e=>C.value.name=e),clearable:"",style:{height:"35px"}},null,8,["modelValue"])])),_:1}),(0,t.Wm)(x,{type:"primary",onClick:y},{default:(0,t.w5)((()=>[(0,t.Uk)("搜索")])),_:1}),(0,t.Wm)(x,{color:"#008997",type:"success",onClick:i[1]||(i[1]=e=>V())},{default:(0,t.w5)((()=>[(0,t.Uk)("添加")])),_:1})])),_:1}),(0,t.Wm)(P,{data:l.value,stripe:"",style:{width:"100%"}},{default:(0,t.w5)((()=>[(0,t.Wm)(q,{type:"selection",width:"55"}),(0,t.Wm)(q,{prop:"name",label:"数据源名称",width:"150",align:"center"}),(0,t.Wm)(q,{prop:"type",label:"数据库类型",width:"150",formatter:h,align:"center"}),(0,t.Wm)(q,{prop:"createdTime",label:"创建时间",width:"180",align:"center"}),(0,t.Wm)(q,{prop:"creater",label:"创建人",width:"180",align:"center"}),(0,t.Wm)(q,{prop:"modifiedTime",label:"修改时间",width:"180",align:"center"}),(0,t.Wm)(q,{prop:"modifier",label:"修改人",align:"center",width:"180"}),(0,t.Wm)(q,{prop:"data",label:"操作",align:"center"},{default:(0,t.w5)((e=>[(0,t._)("span",{class:"text",onClick:a=>V(e.row.id)},"编辑",8,r),(0,t._)("span",{class:"text",onClick:a=>{_.value=e.row.id,v.value=!0}},"删除",8,n),(0,t.wy)(((0,t.wg)(),(0,t.iD)("span",{class:"text ccc","element-loading-text":"拼命加载中",onClick:a=>W(e.row.id)},[(0,t.Uk)("连接")],8,p)),[[N,g.value]])])),_:1})])),_:1},8,["data"]),(0,t.Wm)(Z,{currentPage:C.value.current,"onUpdate:currentPage":i[2]||(i[2]=e=>C.value.current=e),"page-size":C.value.size,"onUpdate:page-size":i[3]||(i[3]=e=>C.value.size=e),"page-sizes":[10,20,30,40,50],layout:"total, sizes, prev, pager, next, jumper",total:a.value,onSizeChange:k,onCurrentChange:U},null,8,["currentPage","page-size","total"]),(0,t.Wm)((0,u.SU)(d["default"]),{modelValue:s.value,"onUpdate:modelValue":i[4]||(i[4]=e=>s.value=e),dialogVisible:s.value,id:f.value,dialogTitle:w.value,onInitUserList:y},null,8,["modelValue","dialogVisible","id","dialogTitle"]),(0,t.Wm)(S,{modelValue:v.value,"onUpdate:modelValue":i[7]||(i[7]=e=>v.value=e),title:"提示",width:"30%"},{footer:(0,t.w5)((()=>[(0,t._)("span",c,[(0,t.Wm)(x,{onClick:i[5]||(i[5]=e=>v.value=!1)},{default:(0,t.w5)((()=>[(0,t.Uk)("取消")])),_:1}),(0,t.Wm)(x,{type:"primary",onClick:i[6]||(i[6]=e=>b())},{default:(0,t.w5)((()=>[(0,t.Uk)(" 确认 ")])),_:1})])])),default:(0,t.w5)((()=>[m])),_:1},8,["modelValue"])],64)}}},w=l(89);const f=(0,w.Z)(v,[["__scopeId","data-v-22149f90"]]);var g=f}}]);
//# sourceMappingURL=691.f5781bf6.js.map