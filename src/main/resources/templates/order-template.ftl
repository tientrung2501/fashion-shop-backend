<!DOCTYPE html>
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Email Confirmation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style type="text/css">
        /**
         * Google webfonts. Recommended to include the .woff version for cross-client compatibility.
         */
        @media screen {
            @font-face {
                font-family: 'Source Sans Pro';
                font-style: normal;
                font-weight: 400;
                src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');
            }

            @font-face {
                font-family: 'Source Sans Pro';
                font-style: normal;
                font-weight: 700;
                src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');
            }
        }

        /**
         * Avoid browser level font resizing.
         * 1. Windows Mobile
         * 2. iOS / OSX
         */
        body,
        table,
        td,
        a {
            -ms-text-size-adjust: 100%; /* 1 */
            -webkit-text-size-adjust: 100%; /* 2 */
        }

        /**
         * Remove extra space added to tables and cells in Outlook.
         */
        table,
        td {
            mso-table-rspace: 0pt;
            mso-table-lspace: 0pt;
        }

        /**
         * Better fluid images in Internet Explorer.
         */
        img {
            -ms-interpolation-mode: bicubic;
        }

        /**
         * Remove blue links for iOS devices.
         */
        a[x-apple-data-detectors] {
            font-family: inherit !important;
            font-size: inherit !important;
            font-weight: inherit !important;
            line-height: inherit !important;
            color: inherit !important;
            text-decoration: none !important;
        }

        /**
         * Fix centering issues in Android 4.4.
         */
        div[style*="margin: 16px 0;"] {
            margin: 0 !important;
        }

        body {
            width: 100% !important;
            height: 100% !important;
            padding: 0 !important;
            margin: 0 !important;
        }

        /**
         * Collapse table borders to avoid space between cells.
         */
        table {
            border-collapse: collapse !important;
        }

        a {
            color: #1a82e2;
        }

        img {
            height: auto;
            line-height: 100%;
            text-decoration: none;
            border: 0;
            outline: none;
        }
    </style>

</head>
<body style="background-color: #e9ecef;">

<!-- start preheader -->
<div class="preheader"
     style="display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;">
    Đơn hàng ${orderId} của bạn đã được xác nhận
</div>
<!-- end preheader -->

<!-- start body -->
<table border="0" cellpadding="0" cellspacing="0" width="100%">

    <!-- start logo -->
    <tr>
        <td align="center">
            <!--[if (gte mso 9)|(IE)]>
            <table align="center" border="0" cellpadding="0" cellspacing="0" width="600">
                <tr>
                    <td align="center" valign="top" width="600">
            <![endif]-->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                <tr>
                    <td align="center" valign="top">
                        <h1 style="margin-TOP: 30px; font-size: 52px; font-weight: 700; letter-spacing: -1px; line-height: 48px;font-family: Arial, sans-serif;">
                            FASHION STORE</h1>
                    </td>
                </tr>
            </table>
            <!--[if (gte mso 9)|(IE)]>
            </td>
            </tr>
            </table>
            <![endif]-->
        </td>
    </tr>
    <!-- end logo -->

    <!-- start hero -->
    <tr>
        <td align="center" bgcolor="#e9ecef">
            <!--[if (gte mso 9)|(IE)]>
            <table align="center" border="0" cellpadding="0" cellspacing="0" width="600">
                <tr>
                    <td align="center" valign="top" width="600">
            <![endif]-->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                <tr>
                    <td align="center" style=" background-color: #ffffff;" bgcolor="#ffffff">
                        <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%"
                               style="max-width:600px;">
                            <tr>
                                <td align="center"
                                    style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;">
                                    <img src="https://img.icons8.com/carbon-copy/100/000000/checked-checkbox.png"
                                         width="125" height="120" style="display: block; border: 0px;"/><br>
                                    <h2 style="font-size: 30px; font-weight: 800; line-height: 36px; color: #333333; margin: 0;">
                                        ${title}
                                    </h2>
                                </td>
                            </tr>
                        </table>
                        <!--[if (gte mso 9)|(IE)]>
                        </td>
                        </tr>
                        </table>
                        <![endif]-->
                    </td>
                </tr>
                <!-- end hero -->

                <!-- start copy block -->
                <tr>
                    <td bgcolor="#fff" align="left" style="padding-top: 20px; padding-right: 20px; padding-left: 20px;">
                        <table cellspacing="0" cellpadding="0" border="0" width="100%">
                            <tr>
                                <td width="75%" align="left" bgcolor="#eeeeee"
                                    style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;">
                                    Đơn hàng
                                </td>
                                <td width="25%" align="left" bgcolor="#eeeeee"
                                    style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px;">
                                    ${orderId}
                                </td>
                            </tr>
                            <#list items?keys as key>
                                <tr>
                                    <td bgcolor="#fff" width="75%" align="left"
                                        style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;">
                                        ${key}
                                    </td>
                                    <td bgcolor="#fff" width="25%" align="left"
                                        style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding: 15px 10px 5px 10px;">
                                        ${items[key]}
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="#fff" align="left" style="padding-top: 20px; padding-right: 20px; padding-left: 20px;">
                        <table cellspacing="0" cellpadding="0" border="0" width="100%">
                            <tr>
                                <td bgcolor="#fff" width="75%" align="left"
                                    style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;">
                                    TỔNG CỘNG
                                </td>
                                <td bgcolor="#fff" width="25%" align="left"
                                    style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 800; line-height: 24px; padding: 10px; border-top: 3px solid #eeeeee; border-bottom: 3px solid #eeeeee;">
                                    ${total}
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- end copy block -->
                <tr>
                    <td align="center" height="100%" valign="top" width="100%"
                        style="padding: 0 35px 35px 35px; background-color: #ffffff;" bgcolor="#ffffff">
                        <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%"
                               style="max-width:660px;">
                            <tr>
                                <td align="center" valign="top" style="font-size:0;">
                                    <div style="display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;">

                                        <table align="left" border="0" cellpadding="0" cellspacing="0" width="100%"
                                               style="max-width:300px;">
                                            <tr>
                                                <td align="left" valign="top"
                                                    style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px;">
                                                    <p style="font-weight: 800;">Hình thức thanh toán:</p>
                                                    <p style="font-weight: 800;">Tình trạng thanh toán:</p>
                                                    <p style="font-weight: 800;">Thời gian dự kiến giao hàng:</p>
                                                    <p style="font-weight: 800;">Người nhận:</p>
                                                    <p style="font-weight: 800;">Số điện thoại:</p>
                                                    <p style="font-weight: 800;">Địa chỉ:</p>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    <div style="display:inline-block; max-width:50%; min-width:240px; vertical-align:top; width:100%;">
                                        <table align="left" border="0" cellpadding="0" cellspacing="0" width="100%"
                                               style="max-width:300px;">
                                            <tr>
                                                <td align="left" valign="top" style="text-align: right;
    padding-right: 20px;font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px;">
                                                    <p>${paymentType}</p>
                                                    <p>${isPaid}</p>
                                                    <p>${expectedTime}</p>
                                                    <p>${name}</p>
                                                    <p>${phone}</p>
                                                    <p>${address}</p>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>

                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td align="center" style=" padding: 35px; background-color: #F5C33B;">
                        <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%"
                               style="max-width:600px;">
                            <tr>
                                <td align="center"
                                    style="font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 16px; font-weight: 400; line-height: 24px; padding-top: 25px;">
                                    <h2 style="font-size: 24px; font-weight: 800; line-height: 30px; color: #ffffff; margin: 0;">
                                        Cảm ơn bạn đã đặt hàng trên website của chúng tôi!
                                    </h2>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" style="padding: 25px 0 15px 0;">
                                    <table border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td align="center" style="border-radius: 5px;" bgcolor="#66b3b7">
                                                <a href="localhost:3000/" target="_blank"
                                                   style="font-size: 18px; font-family: Open Sans, Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; border-radius: 5px; background-color: #000; padding: 15px 30px; border: 1px solid; display: block;">Tiếp
                                                    tục mua sắm</a>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- start footer -->
                <tr>
                    <td align="center" bgcolor="#e9ecef" style="padding: 24px;">
                        <!--[if (gte mso 9)|(IE)]>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" width="600">
                            <tr>
                                <td align="center" valign="top" width="600">
                        <![endif]-->
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                            <!-- start unsubscribe -->
                            <tr>
                                <td align="center" bgcolor="#e9ecef"
                                    style="padding: 12px 24px; font-family: sans-serif; font-size: 14px; line-height: 20px; color: #666;">
                                    <p style="margin: 0;">© 2023 Nhóm 33 Khóa Luận Tốt Nghiệp, Thành phố Hồ Chí Minh</p>
                                </td>
                            </tr>
                            <!-- end unsubscribe -->

                        </table>
                        <!--[if (gte mso 9)|(IE)]>
                        </td>
                        </tr>
                        </table>
                        <![endif]-->
                    </td>
                </tr>
                <!-- end footer -->
            </table>
            </table>
            <!-- end body -->
</body>
</html>