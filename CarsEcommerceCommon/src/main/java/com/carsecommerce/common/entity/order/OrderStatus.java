package com.carsecommerce.common.entity.order;

public enum OrderStatus {
	NEW {
		@Override
		public String defaultDescription() {
			return "Porosia është bërë nga konsumatori";
		}

	}, 

	CANCELLED {
		@Override
		public String defaultDescription() {
			return "Porosia u refuzua";
		}
	}, 

	PROCESSING {
		@Override
		public String defaultDescription() {
			return "Porosia është duke u përpunuar";
		}
	},
	
	PACKAGED {
		@Override
		public String defaultDescription() {
			return "Produktet janë të paketuara";
		}		
	},
	
	PAID {
		@Override
		public String defaultDescription() {
			return "Konsumatori e ka paguar këtë porosi";
		}		
	}, 

	REFUNDED {
		@Override
		public String defaultDescription() {
			return "Konsumatori është rimbursuar";
		}		
	};

	public abstract String defaultDescription();
}
