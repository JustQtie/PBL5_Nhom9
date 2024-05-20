import "./widgetsingle.scss";
import KeyboardArrowUpOutlinedIcon from '@mui/icons-material/KeyboardArrowUpOutlined';
import AccountBalanceWalletOutlinedIcon from '@mui/icons-material/AccountBalanceWalletOutlined';
import ShoppingCartOutlinedIcon from '@mui/icons-material/ShoppingCartOutlined';


const WidgetSingle = ({ type }) => {
    let data = {};

    // temorary
    const amount = 230;


    switch (type) {

        case "order":
            data = {
                title: "Tổng giao dịch thành công",

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
                title: "Tổng giao dịch thất bại",

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
        <div className="widget-single">
            <div className="left-single">
                <span className="title-widget-single">{data.title}</span>
                <span className="counter-single">{amount}</span>

            </div>
            <div className="right-single">
                <div className="percentage-single positive-single">
                    <KeyboardArrowUpOutlinedIcon />
                </div>
                {data.icon}
            </div>
        </div>
    )
}

export default WidgetSingle;