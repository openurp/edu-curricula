[#ftl]
{
  "courses" : [
                [#list users! as user]{ "id" : "${user.id}", "name" : "${user.name?js_string}", "code" : "${user.code?js_string}"}[#if user_has_next],[/#if][/#list]
              ]
}
