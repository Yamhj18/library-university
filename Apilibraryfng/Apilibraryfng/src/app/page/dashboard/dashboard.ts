import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ChartModule } from 'primeng/chart';
import { Api } from '../../api/api';
import { apidashboardStats, apidashboardCharts } from '../../api/functions';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, RouterLink, CardModule, ChartModule],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
    
    stats: any = {
        totalBooks: 0,
        totalStudents: 0,
        activeLoans: 0,
        overdueLoans: 0
    };

    chartData: any;
    chartOptions: any;

    isLoading: boolean = true;

    constructor(private api: Api) {}

    ngOnInit(): void {
        this.initChartOptions();
        this.loadDashboardData();
    }

    initChartOptions() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        
        this.chartOptions = {
            plugins: {
                legend: {
                    labels: { color: textColor }
                }
            },
            scales: {
                x: {
                    ticks: { color: documentStyle.getPropertyValue('--text-color-secondary') },
                    grid: { color: documentStyle.getPropertyValue('--surface-border') }
                },
                y: {
                    ticks: { color: documentStyle.getPropertyValue('--text-color-secondary') },
                    grid: { color: documentStyle.getPropertyValue('--surface-border') }
                }
            }
        };
    }

    loadDashboardData(): void {
        this.isLoading = true;
        
        // Cargar stats
        this.api.invoke(apidashboardStats).then((res: any) => {
            const data = typeof res === 'string' ? JSON.parse(res) : res;
            if (data.type === 'success') {
                this.stats = data.stats;
            }
        });

        // Cargar charts
        this.api.invoke(apidashboardCharts).then((res: any) => {
            const data = typeof res === 'string' ? JSON.parse(res) : res;
            if (data.type === 'success') {
                const chartInfo = data.charts;
                this.chartData = {
                    labels: chartInfo.labels,
                    datasets: [
                        {
                            label: 'Préstamos por Mes',
                            backgroundColor: '#3b82f6',
                            data: chartInfo.data
                        }
                    ]
                };
            }
        }).finally(() => {
            this.isLoading = false;
        });
    }
}
