import "./chart.scss"
import { AreaChart, Area, XAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import React, { useState, useEffect } from 'react';

const data = [
    { name: "Tháng 1", "Số lượt giao dịch": 1200 },
    { name: "Tháng 2", "Số lượt giao dịch": 1500 },
    { name: "Tháng 3", "Số lượt giao dịch": 800 },
    { name: "Tháng 4", "Số lượt giao dịch": 1600 },
    { name: "Tháng 5", "Số lượt giao dịch": 900 },
    { name: "Tháng 6", "Số lượt giao dịch": 2200 }
];

const Chart = ({ aspect, title }) => {

    const [chartData, setChartData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            // try {
            //     // Gọi API để lấy dữ liệu
            //     const response = await axios.get('YOUR_API_ENDPOINT');
            //     // Lấy dữ liệu từ response và cập nhật state
            //     setChartData(response.data);
            // } catch (error) {
            //     console.error('Error fetching chart data:', error);
            // }
        };

        fetchData(); // Gọi hàm fetchData khi component mount

    }, []); // Chạy một lần khi component được mount

    return (
        <div className="chart">
            <div className="title-chart">{title} </div>
            <ResponsiveContainer width="100%" aspect={aspect}>
                <AreaChart width={730} height={250} data={data}
                    margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                    <defs>
                        <linearGradient id="total" x1="0" y1="0" x2="0" y2="1">
                            <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8} />
                            <stop offset="95%" stopColor="#8884d8" stopOpacity={0} />
                        </linearGradient>
                    </defs>
                    <XAxis dataKey="name" stroke="gray" />
                    <CartesianGrid strokeDasharray="3 3" className="chartGrid-chart" />
                    <Tooltip />
                    <Area type="monotone" dataKey="Số lượt giao dịch" stroke="#8884d8" fillOpacity={1} fill="url(#total)" />
                </AreaChart>
            </ResponsiveContainer>
        </div>
    )
}

export default Chart;