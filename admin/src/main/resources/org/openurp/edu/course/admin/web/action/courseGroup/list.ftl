[#ftl]
[@b.head/]
[@b.grid items=courseGroups var="courseGroup"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="15%" property="indexno" title="序号"/]
    [@b.col width="20%" property="name" title="名称"/]
    [@b.col width="15%" property="parent.name" title="上级课程类别"/]
    [@b.col width="15%" property="beginOn" title="生效时间"/]
    [@b.col width="15%" title="页面显示颜色"]<span class="sk" style="background: ${courseGroup.color!}"></span>[/@]
  [/@]
[/@]
<style>
  .sk{ height:8px ; width:8px; display:inline-block; margin-right:5px; vertical-align:middle;}
  </style>
[@b.foot/]
