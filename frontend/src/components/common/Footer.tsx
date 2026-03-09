const Footer = () => {
    const currentYear = new Date().getFullYear();

    return (
        <footer className="bg-gray-900 text-white mt-auto">
            <div className="container mx-auto px-4 py-8">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                    {/* About */}
                    <div>
                        <h3 className="text-lg font-semibold mb-4">About ZimID</h3>
                        <p className="text-gray-400 text-sm">
                            The official online booking system for National ID services in Zimbabwe.
                            Book your appointment conveniently and skip the queues.
                        </p>
                    </div>

                    {/* Quick Links */}
                    <div>
                        <h3 className="text-lg font-semibold mb-4">Quick Links</h3>
                        <ul className="space-y-2 text-sm text-gray-400">
                            <li><a href="/booking" className="hover:text-white transition-colors">Book Appointment</a></li>
                            <li><a href="/check-booking" className="hover:text-white transition-colors">Check Booking Status</a></li>
                            <li><a href="/help" className="hover:text-white transition-colors">Help & Support</a></li>
                            <li><a href="https://www.rg.gov.zw" target="_blank" rel="noopener noreferrer" className="hover:text-white transition-colors">
                                Registrar General Website
                            </a></li>
                        </ul>
                    </div>

                    {/* Contact */}
                    <div>
                        <h3 className="text-lg font-semibold mb-4">Contact</h3>
                        <ul className="space-y-2 text-sm text-gray-400">
                            <li>📞 +263 242 795 000</li>
                            <li>📧 info@rg.gov.zw</li>
                            <li>📍 Corner Samora Machel & Fourth St</li>
                            <li>Harare, Zimbabwe</li>
                        </ul>
                    </div>
                </div>

                <div className="border-t border-gray-800 mt-8 pt-6 text-center text-sm text-gray-400">
                    <p>© {currentYear} Registrar General's Office, Republic of Zimbabwe. All rights reserved.</p>
                </div>
            </div>
        </footer>
    );
};

export default Footer;