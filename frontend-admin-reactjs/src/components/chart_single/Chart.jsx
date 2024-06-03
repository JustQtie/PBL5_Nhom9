import "./chart.scss"
import { AreaChart, Area, XAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import React, { useState, useEffect } from 'react';
import { toast } from "react-toastify";
import numeral from 'numeral';

const initialData = [
    { name: "Tháng 1", "Mức chi tiêu": 0 },
    { name: "Tháng 2", "Mức chi tiêu": 0 },
    { name: "Tháng 3", "Mức chi tiêu": 0 },
    { name: "Tháng 4", "Mức chi tiêu": 0 },
    { name: "Tháng 5", "Mức chi tiêu": 0 },
    { name: "Tháng 6", "Mức chi tiêu": 0 },
    { name: "Tháng 7", "Mức chi tiêu": 0 },
    { name: "Tháng 8", "Mức chi tiêu": 0 },
    { name: "Tháng 9", "Mức chi tiêu": 0 },
    { name: "Tháng 10", "Mức chi tiêu": 0 },
    { name: "Tháng 11", "Mức chi tiêu": 0 },
    { name: "Tháng 12", "Mức chi tiêu": 0 },
];

const Chart = ({ aspect, title, dataChuyenDi }) => {
    const [chartData, setChartData] = useState(initialData);

    useEffect(() => {
        if (dataChuyenDi && dataChuyenDi.total_money_user !== undefined) {
            const updatedData = initialData.map((item, index) => {
                if (index === 5) { // Chỉ cập nhật dữ liệu của tháng 6
                    return { ...item, "Mức chi tiêu": dataChuyenDi.total_money_user };
                }
                return item;
            });
            setChartData(updatedData);
        }

    }, [dataChuyenDi]);

    const formatTooltip = (value) => {
        return `${numeral(value * 1000).format('0,0')} VND`;
    };

    return (
        <div className="chart">
            <div className="title-chart">{title}</div>
            <ResponsiveContainer width="100%" aspect={aspect}>
                <AreaChart width={730} height={250} data={chartData}
                    margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                    <defs>
                        <linearGradient id="total" x1="0" y1="0" x2="0" y2="1">
                            <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8} />
                            <stop offset="95%" stopColor="#8884d8" stopOpacity={0} />
                        </linearGradient>
                    </defs>
                    <XAxis dataKey="name" stroke="gray" />
                    <CartesianGrid strokeDasharray="3 3" className="chartGrid-chart" />
                    <Tooltip formatter={formatTooltip} />
                    <Area type="monotone" dataKey="Mức chi tiêu" stroke="#8884d8" fillOpacity={1} fill="url(#total)" />
                </AreaChart>
            </ResponsiveContainer>
        </div>
    )
}

export default Chart;
