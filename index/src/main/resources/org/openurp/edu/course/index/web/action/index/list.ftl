[#ftl]
[@b.head/]
[@b.grid  items=courseBlogs var="courseBlog" ]
  [@b.row]
    [@b.col width="15%" property="course.code" title="课程代码"/]
    [@b.col width="15%" property="course.name" title="课程名称 "]<a onclick="openLayer(${courseBlog.id})">${(courseBlog.course.name)!}</a>[/@]
    [@b.col width="20%" property="department.name" title="开课院系"/]
    [@b.col width="10%" property="course.credits" title="学分"/]
    [@b.col width="10%" property="course.creditHours" title="学时"/]
    [@b.col width="30%" title="授课教师"][#list courseBlog.teachers as teacher]${teacher.name}[#if teacher_has_next],[/#if][/#list][/@]
  [/@]
[/@]
  [@b.div style="display:none"]
    [@b.div id="blogDiv"  /]
  [/@]
<script>
  beangle.load(["jquery-colorbox"]);
  function openLayer(id){
    var url = "${b.url('index!info?id=aaa')}";
    var newUrl = url.replace("aaa",id);
    bg.Go(newUrl,"blogDiv");
    jQuery.colorbox({
      transition:'none',
      opacity:0,
      overlayClose:true,
      width:"600px",
      height:"600px",
      inline:true,
      href:"#blogDiv"
    });
  };
</script>
[@b.foot/]