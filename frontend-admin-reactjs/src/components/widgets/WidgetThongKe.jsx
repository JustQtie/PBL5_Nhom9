import "./widgetthongke.scss";
import KeyboardArrowUpOutlinedIcon from '@mui/icons-material/KeyboardArrowUpOutlined';
import AccountBalanceWalletOutlinedIcon from '@mui/icons-material/AccountBalanceWalletOutlined';
import ShoppingCartOutlinedIcon from '@mui/icons-material/ShoppingCartOutlined';

const WidgetThongKe = ({ type, balanceCount, orderCount }) => {
    let data = {};

    switch (type) {

        case "order":
            data = {
                title: "Tổng giao dịch thành công",
                amount: orderCount,
                icon: (
                    <ShoppingCartOutlinedIcon
                        className="icon"
                        style={{
                            color: "goldenrod",
                            backgroundColor: "rgba(218, 165, 32, 0.2)",
                        }} />),
            };
            break;
        case "balance":
            data = {
                title: "Tổng giao dịch bị hủy",
                amount: balanceCount,
                icon: (
                    <AccountBalanceWalletOutlinedIcon
                        className="icon"
                        style={{
                            color: "purple",
                            backgroundColor: "rgba(128, 0, 128, 0.2)",
                        }}
                    />
                ),
            };
            break;
        default:
            break;
    }



    return (
        <div className="widget-thongke">
            <div className="left-thongke">
                <span className="title-widget-thongke">{data.title}</span>
                <span className="counter-thongke">{data.amount}</span>

            </div>
            <div className="right-thongke">
                <div className="percentage-thongke positive-thongke">
                    <KeyboardArrowUpOutlinedIcon />
                </div>
                {data.icon}
            </div>
        </div>
    )
}

export default WidgetThongKe;