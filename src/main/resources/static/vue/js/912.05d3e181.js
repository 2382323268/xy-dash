"use strict";(self["webpackChunkxy_dash_admin"]=self["webpackChunkxy_dash_admin"]||[]).push([[912],{5912:function(e,a,l){l.r(a),l.d(a,{default:function(){return b}});l(7658);var t=l(3396),n=l(4870),u=l(7412),i=l(1120),r=l(7178);const o=e=>((0,t.dD)("data-v-b51aaa12"),e=e(),(0,t.Cn)(),e),d={key:0},s={key:1},p={key:0},c={key:1},m={key:0},w={key:1},v=["onClick"],g=["onClick"],k=["onClick"],_=o((()=>(0,t._)("span",{style:{"font-size":"larger"}},"您确定要删除这条记录吗？",-1))),h={class:"dialog-footer"},f={class:"dialog-footer"};var W={__name:"index",setup(e){const a=(0,n.iH)(0),l=(0,n.iH)(null),o=((0,n.iH)(!1),(0,n.iH)(!1)),W=((0,n.iH)(""),(0,n.iH)(-1),(0,n.iH)(null)),y=((0,n.iH)(!1),(0,n.iH)(!1)),C=(0,n.iH)(-1),b=async()=>{const e=await u.ZP.get("admin_api/v1/migrations",x.value);l.value=e.data.data.records,a.value=e.data.data.total},z=e=>{i.Z.push({name:"数据迁移操作",params:{id:e}})},H=async()=>{await u.ZP.post("admin_api/v1/migrations/"+C.value+"/delete");o.value=!1,b()},U=async()=>{await u.ZP.post("admin_api/v1/migrations/"+C.value+"/generatingCode?remark="+W.value);y.value=!1,C.value=-1,W.value="",b(),r.z8.success("操作成功！")},V=e=>{x.value.current=1,x.value.size=e,b()},D=e=>{let a=null;return a=e.threadCount?e.threadCount:0,a},q=e=>{x.value.current=e,b()},x=(0,n.iH)({name:"",current:1,size:10});return b(),(e,n)=>{const u=(0,t.up)("el-input"),i=(0,t.up)("el-col"),r=(0,t.up)("el-button"),P=(0,t.up)("el-row"),Z=(0,t.up)("el-table-column"),S=(0,t.up)("el-table"),T=(0,t.up)("el-pagination"),j=(0,t.up)("el-dialog"),I=(0,t.up)("el-form-item"),Y=(0,t.up)("el-form");return(0,t.wg)(),(0,t.iD)(t.HY,null,[(0,t.Wm)(P,{gutter:20,class:"header"},{default:(0,t.w5)((()=>[(0,t.Wm)(i,{span:4},{default:(0,t.w5)((()=>[(0,t.Wm)(u,{placeholder:"请输入数据源名称...",modelValue:x.value.name,"onUpdate:modelValue":n[0]||(n[0]=e=>x.value.name=e),clearable:"",style:{height:"35px"}},null,8,["modelValue"])])),_:1}),(0,t.Wm)(r,{type:"primary",onClick:b},{default:(0,t.w5)((()=>[(0,t.Uk)("搜索")])),_:1}),(0,t.Wm)(r,{color:"#008997",type:"success",onClick:n[1]||(n[1]=e=>z())},{default:(0,t.w5)((()=>[(0,t.Uk)("添加")])),_:1})])),_:1}),(0,t.Wm)(S,{data:l.value,stripe:"",style:{width:"100%"}},{default:(0,t.w5)((()=>[(0,t.Wm)(Z,{type:"selection",width:"55"}),(0,t.Wm)(Z,{prop:"name",label:"名称",width:"150",align:"center"}),(0,t.Wm)(Z,{prop:"port",label:"启动端口号",width:"150",align:"center"}),(0,t.Wm)(Z,{prop:"count",label:"每轮条数",width:"180",align:"center"}),(0,t.Wm)(Z,{prop:"threadCount",label:"线程数量",formatter:D,align:"center",width:"150"}),(0,t.Wm)(Z,{prop:"isDel",label:"是否删除",width:"180",align:"center"},{default:(0,t.w5)((e=>[e.row.isDel?((0,t.wg)(),(0,t.iD)("span",d,"是")):(0,t.kq)("",!0),e.row.isDel?(0,t.kq)("",!0):((0,t.wg)(),(0,t.iD)("span",s,"否"))])),_:1}),(0,t.Wm)(Z,{prop:"sqlSpliec",label:"是否sql拼接",width:"150",align:"center"},{default:(0,t.w5)((e=>[e.row.sqlSpliec?((0,t.wg)(),(0,t.iD)("span",p,"是")):(0,t.kq)("",!0),e.row.sqlSpliec?(0,t.kq)("",!0):((0,t.wg)(),(0,t.iD)("span",c,"否"))])),_:1}),(0,t.Wm)(Z,{prop:"startThread",label:"是否开启线程",align:"center",width:"150"},{default:(0,t.w5)((e=>[e.row.startThread?((0,t.wg)(),(0,t.iD)("span",m,"是")):(0,t.kq)("",!0),e.row.startThread?(0,t.kq)("",!0):((0,t.wg)(),(0,t.iD)("span",w,"否"))])),_:1}),(0,t.Wm)(Z,{prop:"data",label:"操作",align:"center"},{default:(0,t.w5)((e=>[(0,t._)("span",{class:"text",onClick:a=>{C.value=e.row.id,y.value=!0}},"生成",8,v),(0,t._)("span",{class:"text",onClick:a=>z(e.row.id)},"编辑",8,g),(0,t._)("span",{class:"text",onClick:a=>{C.value=e.row.id,o.value=!0}},"删除",8,k)])),_:1})])),_:1},8,["data"]),(0,t.Wm)(T,{currentPage:x.value.current,"onUpdate:currentPage":n[2]||(n[2]=e=>x.value.current=e),"page-size":x.value.size,"onUpdate:page-size":n[3]||(n[3]=e=>x.value.size=e),"page-sizes":[10,20,30,40,50],layout:"total, sizes, prev, pager, next, jumper",total:a.value,onSizeChange:V,onCurrentChange:q},null,8,["currentPage","page-size","total"]),(0,t.Wm)(j,{modelValue:o.value,"onUpdate:modelValue":n[6]||(n[6]=e=>o.value=e),title:"提示",width:"30%"},{footer:(0,t.w5)((()=>[(0,t._)("span",h,[(0,t.Wm)(r,{onClick:n[4]||(n[4]=e=>o.value=!1)},{default:(0,t.w5)((()=>[(0,t.Uk)("取消")])),_:1}),(0,t.Wm)(r,{type:"primary",onClick:n[5]||(n[5]=e=>H())},{default:(0,t.w5)((()=>[(0,t.Uk)(" 确认 ")])),_:1})])])),default:(0,t.w5)((()=>[_])),_:1},8,["modelValue"]),(0,t.Wm)(j,{modelValue:y.value,"onUpdate:modelValue":n[10]||(n[10]=e=>y.value=e),title:"生成代码",width:"20%"},{footer:(0,t.w5)((()=>[(0,t._)("span",f,[(0,t.Wm)(r,{onClick:n[8]||(n[8]=e=>y.value=!1)},{default:(0,t.w5)((()=>[(0,t.Uk)("取消")])),_:1}),(0,t.Wm)(r,{type:"primary",onClick:n[9]||(n[9]=e=>U())},{default:(0,t.w5)((()=>[(0,t.Uk)(" 确认 ")])),_:1})])])),default:(0,t.w5)((()=>[(0,t.Wm)(Y,{"label-width":"50px",stripe:"",style:{width:"260px"}},{default:(0,t.w5)((()=>[(0,t.Wm)(I,{label:"备注"},{default:(0,t.w5)((()=>[(0,t.Wm)(u,{modelValue:W.value,"onUpdate:modelValue":n[7]||(n[7]=e=>W.value=e),clearable:""},null,8,["modelValue"])])),_:1})])),_:1})])),_:1},8,["modelValue"])],64)}}},y=l(89);const C=(0,y.Z)(W,[["__scopeId","data-v-b51aaa12"]]);var b=C}}]);
//# sourceMappingURL=912.05d3e181.js.map