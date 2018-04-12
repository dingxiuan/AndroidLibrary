package com.dxa.common.http;

/**
 * Http的状态码
 */
public enum HttpCode {
    /**
     * 服务器端收到此状态码后，表示收到请求的初始化首部，然后响应Expect首部，
     * 请客户端继续
     */
    _100(100, "Continue"),
    /**
     * 服务器端收到此状态码后，表示收到请求的初始化首部，然后响应Expect首部，
     * 请客户端继续
     */
    CONTINUE(100, "Continue"),
    /**
     * 根据客户端的指定，服务器正将协议切换成Update首部所列的协议
     */
    _101(101, "Switching Protocols"),
    /**
     * 根据客户端的指定，服务器正将协议切换成Update首部所列的协议
     */
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),

    // --------------------------------------------------------------------
    // 请求成功状态

    /**
     * 请求没问题
     */
    _200(200, "OK"),
    /**
     * 请求没问题
     */
    OK(200, "OK"),
    /**
     * 创建服务器资源成功，响应实体中必须包含已创建资源的URL(资源必须创建成功)
     */
    _201(201, "Created"),
    /**
     * 创建服务器资源成功，响应实体中必须包含已创建资源的URL(资源必须创建成功)
     */
    CREATED(201, "Created"),
    /**
     * 请求已接收，但还未处理，不能保证处理成功
     */
    _202(202, "Accepted"),
    /**
     * 请求已接收，但还未处理，不能保证处理成功
     */
    ACCEPTED(202, "Accepted"),
    /**
     * 实体首部包含的信息来自于资源的副本，而非源端服务器资源；
     * 无法或没有对发送的与资源相关的元信息进行验证，就会出现这种情况
     */
    _203(203, "Non-Authoritative Information"),
    /**
     * 实体首部包含的信息来自于资源的副本，而非源端服务器资源；
     * 无法或没有对发送的与资源相关的元信息进行验证，就会出现这种情况
     */
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    /**
     * 响应报文中包含若干首部和一个状态行，没有实体的主体部分
     */
    _204(204, "No Content"),
    /**
     * 响应报文中包含若干首部和一个状态行，没有实体的主体部分
     */
    NO_CONTENT(204, "No Content"),
    /**
     * 告诉浏览器清除当前页面中的所有HTML表单元素
     */
    _205(205, "Reset Content"),
    /**
     * 告诉浏览器清除当前页面中的所有HTML表单元素
     */
    RESET_CONTENT(205, "Reset Content"),
    /**
     * 部分或范围(Range)请求成功，此状态码必须包含Content-Range、Date、ETag
     * 或Content-Location首部
     */
    _206(206, "Patial Content"),
    /**
     * 部分或范围(Range)请求成功，此状态码必须包含Content-Range、Date、ETag
     * 或Content-Location首部
     */
    PATIAL_CONTENT(206, "Patial Content"),

    // --------------------------------------------------------------------
    // 重定向
    /**
     * 客户端的请求指向多个资源的URL，如：有英文和中文的页面
     */
    _300(300, "Multiple Choices"),
    /**
     * 客户端的请求指向多个资源的URL，如：有英文和中文的页面
     */
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    /**
     * 请求的URL已被移除
     */
    _301(301, "Move Permanently"),
    /**
     * 请求的URL已被移除
     */
    MOVE_PERMANENTLY(301, "Move Permanently"),
    /**
     * 与301类似，客户端应该使用Location首部临时定位资源
     */
    _302(302, "Found"),
    /**
     * 与301类似，客户端应该使用Location首部临时定位资源
     */
    FOUND(302, "Found"),
    /**
     * 告诉客户端应该使用另一个URL访问资源
     */
    _303(303, "See Other"),
    /**
     * 告诉客户端应该使用另一个URL访问资源
     */
    SEE_OTHER(303, "See Other"),
    /**
     * 客户端可以通过请求首部使请求变成有条件的请求
     */
    _304(304, "Not Modified"),
    /**
     * 客户端可以通过请求首部使请求变成有条件的请求
     */
    NOT_MODIFIED(304, "Not Modified"),
    /**
     * 必须通过代理访问资源，代理的位置由Location首部给出
     */
    _305(305, "Use Proxy"),
    /**
     * 必须通过代理访问资源，代理的位置由Location首部给出
     */
    USE_PROXY(305, "Use Proxy"),
    /**
     * 未使用
     */
    _306(306, "????"),
    /**
     * 与301相似，使用Location首部给出URL来临时定位资源，Http1.1用此状态取代了302状态码
     */
    _307(307, "Temporary Redirect"),
    /**
     * 与301相似，使用Location首部给出URL来临时定位资源，Http1.1用此状态取代了302状态码
     */
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    // --------------------------------------------------------------------
    // 客户端错误

    /**
     * 错误的请求
     */
    _400(400, "Bad Request"),
    /**
     * 错误的请求
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 未授权
     */
    _401(401, "Unauthorized"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * 未使用
     */
    _402(402, "Payment Required"),
    /**
     * 未使用
     */
    PAYMENT_REQUIRED(402, "Payment Required"),
    /**
     * 请求被服务器拒绝
     */
    _403(403, "Forbidden"),
    /**
     * 请求被服务器拒绝
     */
    FORBIDDEN(403, "Forbidden"),
    /**
     * 无法找到请求的URL
     */
    _404(404, "Not Found"),
    /**
     * 无法找到请求的URL
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * URL不支持的方法，响应中应该包含Allow首部，告诉客户端包含请求的资源
     */
    _405(405, "Method Not Allow"),
    /**
     * URL不支持的方法，响应中应该包含Allow首部，告诉客户端包含请求的资源
     */
    METHOD_NOT_ALLOW(405, "Method Not Allow"),
    /**
     * 客户端通过指定参数来说明愿意接收的实体类型，服务器端没有客户端指定的实体类型时，
     * 使用此状态码
     */
    _406(406, "Not Acceptable"),
    /**
     * 客户端通过指定参数来说明愿意接收的实体类型，服务器端没有客户端指定的实体类型时，
     * 使用此状态码
     */
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    /**
     * 与401类似，用于要求对资源进行认证的代理服务器
     */
    _407(407, "Proxy Authentication Required"),
    /**
     * 与401类似，用于要求对资源进行认证的代理服务器
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    /**
     * 客户端完成请求的时间太长，服务器可以返回此状态码，并关闭连接
     */
    _408(408, "Request Timeout"),
    /**
     * 客户端完成请求的时间太长，服务器可以返回此状态码，并关闭连接
     */
    REQUEST_TIMEOUT(408, "Request Timeout"),
    /**
     * 请求在资源上可能引起冲突
     */
    _409(409, "Conflict"),
    /**
     * 请求在资源上可能引起冲突
     */
    CONFLICT(409, "Conflict"),
    /**
     * 服务器曾用过此资源，主要用于Web站点的维护
     */
    _410(410, "Gone"),
    /**
     * 服务器曾用过此资源，主要用于Web站点的维护
     */
    GONE(410, "Gone"),
    /**
     * 服务器要求在请求报文中包含Content-Length首部
     */
    _411(411, "Length Required"),
    /**
     * 服务器要求在请求报文中包含Content-Length首部
     */
    LENGTH_REQUIRED(411, "Length Required"),
    /**
     * 客户端发起的条件请求中含有失败条件（客户端请求包含Expect首部，即为条件请求）
     */
    _412(412, "Precondition Failed"),
    /**
     * 客户端发起的条件请求中含有失败条件（客户端请求包含Expect首部，即为条件请求）
     */
    PRECONDITION_FAILED(412, "Precondition Failed"),
    /**
     * 客户端发送请求的实体主体部分比服务器端能够或者预期处理的要大
     */
    _413(413, "Request Entity Too Large"),
    /**
     * 客户端发送请求的实体主体部分比服务器端能够或者预期处理的要大
     */
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
    /**
     * 请求的URI太长
     */
    _414(414, "Eequest URI Too Long"),
    /**
     * 请求的URI太长
     */
    EEQUEST_URI_TOO_LONG(414, "Eequest URI Too Long"),
    /**
     * 不支持的媒体类型
     */
    _415(415, "Unsupported Media Type"),
    /**
     * 不支持的媒体类型
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    /**
     * 范围无效或无法满足请求报文指定的资源
     */
    _416(416, "Requested Range Not Satisfiable"),
    /**
     * 范围无效或无法满足请求报文指定的资源
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
    /**
     * 请求的Expect首部包含一个期望，服务器无法满足此期望
     */
    _417(417, "Expectation Failed"),
    /**
     * 请求的Expect首部包含一个期望，服务器无法满足此期望
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),

    // --------------------------------------------------------------------
    // 服务器错误

    /**
     * 服务器内部错误
     */
    _500(500, "Internal Server Error"),
    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    /**
     * 客户端发送的请求超出服务器的能力范围
     */
    _501(501, "Not Implemented"),
    /**
     * 客户端发送的请求超出服务器的能力范围
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),
    /**
     * 代理或网关使用的服务器从响应链的下一条链路上收到了一条伪请求
     */
    _502(502, "Bad Gateway"),
    /**
     * 代理或网关使用的服务器从响应链的下一条链路上收到了一条伪请求
     */
    BAD_GATEWAY(502, "Bad Gateway"),
    /**
     * 服务器现在无法为请求提供服务（将来可以提供）
     */
    _503(503, "Service Unavaiable"),
    /**
     * 服务器现在无法为请求提供服务（将来可以提供）
     */
    SERVICE_UNAVAIABLE(503, "Service Unavaiable"),
    /**
     * 网关或代理响应超时
     */
    _504(504, "Gateway Timeout"),
    /**
     * 网关或代理响应超时
     */
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    /**
     * Http版本不支持
     */
    _505(505, "Http Version Not Supported"),
    /**
     * Http版本不支持
     */
    HTTP_VERSION_NOT_SUPPORTED(505, "Http Version Not Supported"),

    ;

    private final int code;
    private final String description;

    private HttpCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
