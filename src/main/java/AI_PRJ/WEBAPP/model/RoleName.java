package AI_PRJ.WEBAPP.model;

/**
 * =============================================
 * ENUM: ROLES TRONG HỆ THỐNG STEM KIT
 * =============================================
 *
 *  ADMIN: Quản trị viên hệ thống
 *    - Quản lý toàn bộ hệ thống
 *    - Quản lý users và phân quyền
 *    - Truy cập tất cả chức năng
 * 
 *  MANAGER: Quản lý kinh doanh  
 *    - Quản lý sản phẩm KIT và bài Lab
 *    - Quản lý giao nhận Kit vật lý
 *    - Quản lý tài khoản người dùng
 *    - Báo cáo và thống kê
 * 
 *  STAFF: Nhân viên hỗ trợ kỹ thuật
 *    - Hỗ trợ kỹ thuật bài Lab cho khách hàng
 *    - Quản lý lịch sử hỗ trợ
 *    - Theo dõi số lần hỗ trợ
 * 
 *  CUSTOMER: Khách hàng
 *    - Mua sắm sản phẩm KIT
 *    - Truy cập bài LAB (sau khi mua KIT)
 *    - Yêu cầu hỗ trợ kỹ thuật
 */
public enum RoleName {
    ADMIN("Quản trị viên", "Toàn quyền quản lý hệ thống"),
    MANAGER("Quản lý kinh doanh", "Quản lý sản phẩm, giao nhận và báo cáo"), 
    STAFF("Nhân viên hỗ trợ", "Hỗ trợ kỹ thuật và tư vấn khách hàng"),
    CUSTOMER("Khách hàng", "Mua sắm và sử dụng dịch vụ");
    
    private final String displayName;
    private final String description;
    
    RoleName(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}