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
    [@b.col width="15%" property="code" title="代码"]${courseGroup.code}[/@]
    [@b.col width="20%" property="name" title="名称"/]
    [@b.col width="15%" property="parent.name" title="上级课程类别"/]
    [@b.col width="15%" property="beginOn" title="生效时间"]${courseGroup.beginOn!}[/@]
    [@b.col width="15%" property="endOn" title="失效时间"]${courseGroup.endOn!}[/@]
  [/@]
[/@]
[@b.foot/]
