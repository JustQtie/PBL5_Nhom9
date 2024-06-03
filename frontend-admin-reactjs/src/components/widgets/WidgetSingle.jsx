import "./widgetsingle.scss";
import KeyboardArrowUpOutlinedIcon from '@mui/icons-material/KeyboardArrowUpOutlined';
import AccountBalanceWalletOutlinedIcon from '@mui/icons-material/AccountBalanceWalletOutlined';
import ShoppingCartOutlinedIcon from '@mui/icons-material/ShoppingCartOutlined';
import CancelPresentationOutlinedIcon from '@mui/icons-material/CancelPresentationOutlined';
import { useState, useEffect } from 'react';
import numeral from 'numeral';

const WidgetSingle = ({ type, dataChuyenDi }) => {
    let data = {};
    let amountFormatted = '';
    const [amount, setAmount] = useState(0);
    const [successCount, setSuccessCount] = useState(0);
    const [cancelCount, setCancelCount] = useState(0);

    useEffect(() => {
        if (dataChuyenDi) {
            setAmount(dataChuyenDi.total_money_user);
            setSuccessCount(dataChuyenDi.total_order_success_user);
            setCancelCount(dataChuyenDi.total_order_canceled_user);
        }
    }, [dataChuyenDi]);

    switch (type) {
        case "chitieu":
            amountFormatted = numeral(amount * 1000).format('0,0');
            data = {
                title: "Số tiền đã chi tiêu",
                icon: (
                    <AccountBalanceWalletOutlinedIcon
                        className="icon"
                        style={{
                            color: "goldenrod",
                            backgroundColor: "rgba(218, 165, 32, 0.2)",
                        }}
                    />
                ),
                value: `${amountFormatted} VND`,
            };
            break;
        case "thanhcong":
            data = {
                title: "Số giao dịch thành công",
                icon: (
                    <ShoppingCartOutlinedIcon
                        className="icon"
                        style={{
                            color: "purple",
                            backgroundColor: "rgba(128, 0, 128, 0.2)",
                        }}
                    />
                ),
                value: successCount,
            };
            break;
        case "thatbai":
            data = {
                title: "Số giao dịch thất bại",
                icon: (
                    <CancelPresentationOutlinedIcon
                        className="icon"
                        style={{
                            color: "red",
                            backgroundColor: "rgba(128, 0, 128, 0.2)",
                        }}
                    />
                ),
                value: cancelCount,
            };
            break;
        default:
            break;
    }

    return (
        <div className="widget-single">
            <div className="left-single">
                <span className="title-widget-single">{data.title}</span>
                <span className="counter-single">{data.value}</span>
            </div>
            <div className="right-single">
                <div className="percentage-single positive-single">
                    <KeyboardArrowUpOutlinedIcon />
                </div>
                {data.icon}
            </div>
        </div>
    );
}

export default WidgetSingle;
