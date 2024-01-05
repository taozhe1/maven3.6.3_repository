$(function (){

    $('tbody').on('click', '.delete-btn', function() {
            var userId = $(this).data('id');
            var isConfirmed = confirm('确定要删除这个用户吗？');
            if(isConfirmed){
                $.ajax({
                    url:baseUrl+'user/del?id='+userId,
                    type: 'DELETE',
                    async:true,
                    success: function(result) {
                        if (result) {
                            alert('用户已删除');
                            window.location.reload();
                        }
                    },error: function() {
                        // 处理删除失败的逻辑
                        alert('删除失败');
                    }
                });
            }
        });

    $('tbody').on('click', '.edit-btn', function() {
            var userId = $(this).data('id');
            // 获取用户数据的 Ajax 调用
            $.ajax({
                url:baseUrl+'user/getById?id='+userId,
                type: 'GET',
                async:true,
                success: function(user) {
                    $('#userId').val(user.id);
                    $('#userName').val(user.userName);
                    $('#password').val(user.password);
                    $('#editModal').modal('show'); // 显示模态框
                }
            });
        });

        $('#editForm').submit(function(e) {
        e.preventDefault();
            var formData = {
                id: $('#userId').val(),
                userName: $('#userName').val(),
                password: $('#password').val(),
                // 包含其他字段
            };
        // 提交表单的 Ajax 调用
        $.ajax({
        url:baseUrl+'user/update',
        type: 'POST',
        dataType:'json',
        contentType: 'application/json', // 设置发送数据的格式
        data: JSON.stringify(formData), // 将对象转换为 JSON 字符串   使用 JSON.stringify() 将 JavaScript 对象转换为 JSON 字符串，因为 @RequestBody 需要一个 JSON 格式的请求体。
        success: function(result) {
        $('#editModal').hide();
        window.location.reload(); // 或其他更新 UI 的逻辑
        },error: function() {
                // 处理删除失败的逻辑
                alert('修改失败');
            }
    });
    });


    $('#searchButton').click(function() {
        var searchQuery = $('#searchInput').val();
        $.ajax({
            url: baseUrl + 'user/getByName',
            //url: baseUrl + 'user/getByName?name='+searchQuery,用于单个参数，多个用这种，便于维护 data: { username: searchQuery },
            type: 'GET',
            data: { userName: searchQuery },
            dataType: 'json',  // 指定服务器返回的数据类型
            success: function(users) {
                // 清空现有用户列表
                $('tbody').empty();

                // 将搜索结果添加到用户列表
                users.forEach(function(user) {
                    var userRow = `
            <tr>
                <td>${user.id}</td>
                <td>${user.userName}</td>
                <td>${user.password}</td>
                <td>${user.lastLogin}</td>
                <td>${user.lastIp}</td>
                <td>${user.status === 0 ? '禁用' : '启用'}</td>
                <td>${user.salt}</td>
                <td>
                    <button data-id="${user.id}" class="delete-btn">删除</button>
                    <button data-id="${user.id}" class="edit-btn">修改</button>
                </td>
            </tr>`;
                    $('tbody').append(userRow);
                });
            }

        });
    });

    $('#addUserForm').submit(function(event) {
        event.preventDefault();
        var userData = {
            userName: $('#addUsername').val(),
            password: $('#addPassword').val()
            // 其他字段
        };

        $.ajax({
            url: baseUrl + 'user/save',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function(response) {
                alert('添加用户成功');
                $('#addUserModal').modal('hide');
                loadPage(1); // 重新加载当前页
            },
            error: function() {
                alert('添加用户失败');
            }
        });
    });

    $(document).ready(function() {
        $('#loginForm').submit(function(event) {
            event.preventDefault();
            var userName = $('#userName').val();
            var password = $('#password').val();

            $.ajax({
                url:baseUrl+'user/loginJs',
                type: 'POST',
                async:true,
                data: {
                    userName: userName,
                    password: password
                },
                success: function(response) {
                    if(response){
                     window.location.href = baseUrl+'user/list'; // 登录成功后跳转到查询页面
                    }
                },
                error: function(response) {
                    // 处理登录失败的情况
                    alert('登录失败');
                }
            });
        });
    });

});

