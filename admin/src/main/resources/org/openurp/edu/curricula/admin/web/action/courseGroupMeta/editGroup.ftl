[#ftl]
[@b.head/]
[@b.toolbar title="维护课程分组"]bar.addBack();[/@]
[@b.tabs]
  [@b.form action="!saveGroup?metaIds="+${metaIds} theme="list"]
    [@b.select name="courseGroup.id" label="课程分组" items=courseGroups?sort_by("code")  option="id,name" empty="..."/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]
