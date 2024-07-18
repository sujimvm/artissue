//닉네임 기업,개인 선택
$(document).ready(function() {
    var isCompanyNumberChecked = false;

    var selectedValue = $('input[name="userType"]:checked').val();
    updateInputFields(selectedValue);

    //선택 변경시
    $('body').on('change', 'input[name="userType"]', function() {
        var selectedValue = $(this).val();
        updateInputFields(selectedValue);
    });

    function updateInputFields(selectedValue) {
        if (selectedValue == 'company') {
            $('#inputContainer').html(`
                <label for="companyName">기업명 </label>
                <input id="companyName" name="member_nickname" class="form-control" required minlength="2" maxlength="10" placeholder="기업명을 입력해주세요" required>
                <br><span id="charCount"></span>
                <div class="mt-3">
                    <label for="businessNumber">사업자등록번호 </label>
                    <div class="input-group">
                        <input type="text" id="company_number" name="company_number" class="form-control testWidth" placeholder="사업자등록번호를 입력해주세요" required>
                        <button class="btn btn-outline-secondary widBt" id="company_number_btn">조회</button>
                    </div>
                    <span id="companyNumberCheckmark" class="checkmark" style="display: none; float: left; margin-right: 5px;">✔</span>
                </div>
            `);
            isCompanyNumberChecked = false;
        } else if (selectedValue == 'individual') {
            $('#inputContainer').html('<label for="nickname">닉네임 </label><input id="nickname" name="member_nickname" class="form-control" required minlength="2" maxlength="10" placeholder="닉네임을 입력해주세요" required><br><span id="charCount"></span>');
        }
    }

    // 글자수 제한 및 실시간 유효성 검사
    $(document).on('input', 'input[name="member_nickname"]', function() {
        var charCount = $(this).val().length;

        if (charCount < 2 || charCount > 10) {
            $(this).addClass('is-invalid');
            $('#charCount').text('닉네임을 2~10자로 입력해주세요(' + charCount + '/10글자)');
            $('#charCount').css('color', 'red');
        } else {
            $(this).removeClass('is-invalid');
            $('#charCount').text('');
        }
    });

    // 사업자번호 유효성검사
    $(document).on('input', '#company_number', function() {
        isCompanyNumberChecked = false;
        $('#companyNumberCheckmark').hide();
    });

    $(document).on('click', '#company_number_btn', function(event) {
        event.preventDefault();  // 폼 제출 방지
        var data = $("#company_number").val();
        console.log("Input data:", data);
        var data1 = { "b_no": [data] };
        console.log("data1:", data1);

        $.ajax({
            url: "https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=cjlBlZd9MpuLsL8wGluvUW%2F2cbWcUcNP7nr%2FuWO7Gbl%2Be7Li1KYbOlQm3cWN0GKnxnFI%2BjSewI3AAMiTKFJk0Q%3D%3D",
            type: "POST",
            data: JSON.stringify(data1),
            dataType: "json",
            contentType: "application/json",
            success: function(result) {
                console.log("Result from first API:", result);
                var data2 = result.data[0].tax_type_cd;

                if (data2 === "") {
                    $('#companyNumberCheckmark').hide();
                    alert("비정상적인 사업자 등록번호입니다.");
                    isCompanyNumberChecked = false;
                } else {
                    $.ajax({
                        url: "/ajax/companyNumberCheck",
                        data: { company_no: data },
                        type: "POST",
                        dataType: "text",
                        success: function(result) {
                            console.log("Result from second API:", result);
                            if (result === "available") {
                                $('#companyNumberCheckmark').show();
                                alert("사용 가능한 사업자등록번호입니다.");
                                isCompanyNumberChecked = true;
                            } else {
                                $('#companyNumberCheckmark').hide();
                                alert("이미 사용 중인 사업자등록번호입니다.");
                                isCompanyNumberChecked = false;
                            }
                        },
                        error: function(xhr, status, error) {
                            console.error("Error from second API:", status, error);
                            alert("데이터 통신 오류입니다.");
                            isCompanyNumberChecked = false;
                        }
                    });
                }
            },
            error: function(xhr, status, error) {
                console.error("Error from first API:", status, error);
                alert("데이터 통신 오류입니다.");
                isCompanyNumberChecked = false;
            }
        });
    });

    // 폼 제출 이벤트 처리
    $('form').on('submit', function(event) {
        var selectedValue = $('input[name="userType"]:checked').val();
        if (selectedValue === 'company' && !isCompanyNumberChecked) {
            event.preventDefault();
        }
    });
});

//비밀번호 체크
$(function(){
    $('#Password1').on('input', function() {
        var pwd = $(this).val();
        var pwdPattern = /^(?=.*[!@#$%^&*(),.?":{}|<>])[A-Za-z\d!@#$%^&*(),.?":{}|<>]{8,20}$/;
        if (pwdPattern.test(pwd)) {
            $('#pwdCheckmark').show();
            $('#pwdError').hide();
        } else {
            $('#pwdCheckmark').hide();
            $('#pwdError').show();
        }
    });
});

$(function(){
    var pwd_check = false;
    $("#Password2").on('input', function(){
        pwdCheck($(this).val());
    });

    function pwdCheck(pwd){
        if(pwd==""){
            $("#pwdCheckOk").hide();
            $("#pwdEheckErr").hide();
            return;
        }

        if($("#Password2").val() === $("#Password1").val()){
            $("#pwdCheckOk").show();
            $("#pwdEheckErr").hide();
        }else{
            $("#pwdCheckOk").hide();
            $("#pwdEheckErr").show();
        }
    };
});


//중복 아이디 검사
let isIdChecked = false; // 전역 변수로 아이디 중복 확인 상태 추가

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('Idcheckmark').style.display = 'none';
    document.getElementById('checkIdBtn').addEventListener('click', function(event) {
        event.preventDefault();
        checkId();
    });

    document.getElementById("member_id").addEventListener("input", function() {
        document.getElementById('Idcheckmark').style.display = 'none';
        isIdChecked = false; // 입력 변경 시 중복 확인 상태 리셋
    });
});

function checkId() {
    var memberId = document.getElementById("member_id").value;

    axios.post("/veri/checkId", null, { params: { id: memberId } })
        .then(function(response) {
            var responseData = response.data.trim();
            if (responseData === 'exists') {
                alert("이미 사용 중인 아이디입니다.");
                document.getElementById('Idcheckmark').style.display = 'none';
                isIdChecked = false;
            } else if (responseData === 'available') {
                alert("사용 가능한 아이디입니다.");
                document.getElementById('Idcheckmark').style.display = 'inline';
                isIdChecked = true;
            } else {
                alert("서버에서 알 수 없는 응답을 받았습니다: " + responseData);
                document.getElementById('Idcheckmark').style.display = 'none';
                isIdChecked = false;
            }
        })
        .catch(function(error) {
            console.error("AJAX 요청 실패:", error);
            alert("서버 요청 중 오류가 발생했습니다.");
            document.getElementById('Idcheckmark').style.display = 'none';
            isIdChecked = false;
        });
}

//이메일 유효성검사
$(function(){
    $('#member_email').on('input', function() {
        var email = $(this).val();
        var email_regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
        if (email_regex.test(email)) {
            $('#emailCheck').show();
            $('#emailErr').hide();
        } else {
            $('#emailCheck').hide();
            $('#emailErr').show();
        }
    });
});




function insertCheck(){
    var selectedValue = $('input[name="userType"]:checked').val();

    var isIdValid = $('#Idcheckmark').is(':visible');
    var ispwdValid = $('#pwdCheckmark').is(':visible');
    var ispwd2Valid = $('#pwdCheckOk').is(':visible');
    var isPhoneValid = $('#phoneCheckOkMark').is(':visible');
    debugger;

    if (selectedValue === 'company') {
        var isCompanyNum = $('#companyNumberCheckmark').is(':visible');
        if(!isCompanyNum){
            console.log("Company number check failed");
            alert("사업자번호를 확인해주세요.");
            return false;
        }
    }

    if (!isIdValid) {
        alert("아이디가 유효하지 않습니다.");
        return false;
    }

    if (!ispwdValid) {
        alert("비밀번호가 유효하지 않습니다.");
        return false;
    }

    if (!ispwd2Valid) {
        alert("비밀번호가 유효하지 않습니다.");
        return false;
    }

    if(!isPhoneValid){
        debugger;
        alert('휴대폰 본인인증 번호를 확인하여주십시오.');
        $("#userNum").focus();
        return false;
    }

};



//인증번호 발송
$(document).ready(function() {
    var verificationCode = null; // 초기값을 null로 설정

    $('#phoneCheckOkMark').hide();
    $('#phoneCheckErr').hide();

    $("#send").on("click", function(event) {
        event.preventDefault();

        const memberPhone = $('input[name="veriPhone"]').val().trim();
        const data = { memberPhone: memberPhone };

        axios.post("/veri/send-one", null, { params: data })
            .then(res => {
                alert("문자를 발송했습니다. 인증번호를 확인해주세요.");
                $("#resendbtn").show();
                $("#send").hide();
                verificationCode = res.data.verificationCode; // 최신 인증번호로 업데이트
                $('#checkMessage').show();
                $('#phoneCheckErr').hide();
            })
            .catch(error => {
                alert("데이터 통신 오류입니다.");
                console.error('오류 내용:', error.response ? error.response.data : error.message);
            });
    });

    $("#resendbtn").on('click', function() {
        var mgrPhone = $("#to").val();
        $("#userNum").val('');

        $.ajax({
            url : "veri/reSendSms",
            type : "POST",
            data : { mgrPhone : mgrPhone },
            dataType : "JSON",
            success : function(result) {
                console.log(result);
                if (result.status == 200) {
                    alert("인증번호가 전화번호로 재전송되었습니다.");
                    verificationCode = result.verificationCode; // 최신 인증번호로 업데이트
                    $('#checkMessage').show();
                    $('#phoneCheckOkMark').hide();
                    $('#phoneCheckErr').hide();
                } else {
                    alert("인증번호 전송에 실패했습니다.");
                }
            },
            error : function() {
                alert("데이터 통신 오류입니다.");
            }
        });
    });

    $("#enterBtn").on('click', function() {
        var userNum = $("#userNum").val().trim();

        if (verificationCode && userNum === verificationCode) {
            alert("인증 성공하였습니다.");
            $('#phoneCheckOkMark').show();
            $('#phoneCheckErr').hide();
            $('#checkMessage').hide();
        } else {
            alert("인증 실패하였습니다. 다시 입력해주세요.");
            $('#phoneCheckErr').show();
            $('#phoneCheckOkMark').hide();
            $('#checkMessage').hide();
        }
    });
});

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("userNum").addEventListener("input", function() {
        document.getElementById('phoneCheckOkMark').style.display = 'none';
    });
});

$('#agreeCheckAll').on('change', function () {
    var isChecked = $(this).is(':checked');
    $('#requiredAgreeCheck1').prop('checked', isChecked);
});

