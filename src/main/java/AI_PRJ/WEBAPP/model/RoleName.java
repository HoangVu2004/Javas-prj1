package AI_PRJ.WEBAPP.model;

/**
 * Enum định nghĩa các vai trò trong hệ thống
 * ADMIN: Quản trị viên hệ thống - quyền cao nhất
 * MANAGER: Quản lý - quyền quản lý nhân viên và báo cáo
 * STAFF: Nhân viên - quyền xử lý đơn hàng và hỗ trợ khách hàng
 * CUSTOMER: Khách hàng - quyền mua hàng và sử dụng dịch vụ
 */
public enum RoleName {
    ADMIN("Quản trị viên"),
    MANAGER("Quản lý"), 
    STAFF("Nhân viên"),
    CUSTOMER("Khách hàng");
    
    private final String displayName;
    
    RoleName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}